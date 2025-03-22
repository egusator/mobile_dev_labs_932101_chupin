import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_deadline_manager.model.TaskForScheduleModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleViewModel(
    private val scheduleUseCase: ScheduleUseCase
) : ViewModel() {

    private val _scheduleState = mutableStateOf<ScheduleState>(ScheduleState.Loading)
    val scheduleState: State<ScheduleState> get() = _scheduleState

    private var currentDate: LocalDate? = null

    var isLoading by mutableStateOf(false)
        private set

    fun fetchSchedule(date: LocalDate) {
        isLoading = true
        viewModelScope.launch {
            try {
                val schedule = scheduleUseCase.getScheduleForDate(date)
                _scheduleState.value = ScheduleState.Loaded(schedule)
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error("Ошибка: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun removeTask(taskId: Int?) {
        viewModelScope.launch {
            try {
                scheduleUseCase.removeTask(taskId)  // Удаляем задачу
                fetchSchedule(currentDate ?: LocalDate.now())  // Обновляем расписание
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error("Ошибка при удалении задачи: ${e.message}")
            }
        }
    }
}

sealed class ScheduleState {
    object Loading : ScheduleState()
    data class Loaded(val schedule: List<TaskForScheduleModel>) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}