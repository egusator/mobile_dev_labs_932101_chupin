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
    val scheduleState: State<ScheduleState> = _scheduleState

    private var currentDate  by mutableStateOf(LocalDate.now())

    fun fetchSchedule(date: LocalDate) {
        viewModelScope.launch {
            _scheduleState.value = ScheduleState.Loading
            try {
                val schedule = scheduleUseCase.getScheduleForDate(date)
                _scheduleState.value = ScheduleState.Loaded(schedule)
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error("Ошибка: ${e.message}")
            }
        }
    }

    fun removeTask(taskId: Int?) {
        viewModelScope.launch {
            try {
                scheduleUseCase.removeTask(taskId)
                fetchSchedule(currentDate) // Используем обновленное значение currentDate
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error("Ошибка при удалении задачи: ${e.message}")
            }
        }
    }

    fun createTask(taskId: Int?, workStart: LocalTime, workEnd: LocalTime, date: LocalDate) {
        viewModelScope.launch {
            try {
                scheduleUseCase.createNewTaskForSchedule(taskId, workStart, workEnd, date)
                fetchSchedule(date)
            } catch (e: Exception) {
                _scheduleState.value = ScheduleState.Error("Ошибка при создании задачи: ${e.message}")
            }
        }
    }

    suspend fun getAllTasksForChoose(): List<TaskForChoose> {
        return scheduleUseCase.getAllTasksForChoose()
    }
}

sealed class ScheduleState {
    object Loading : ScheduleState()
    data class Loaded(val schedule: List<TaskForScheduleModel>) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}