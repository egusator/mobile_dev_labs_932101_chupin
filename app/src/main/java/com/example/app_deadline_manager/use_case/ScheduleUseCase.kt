import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.model.TaskForScheduleModel
import com.example.app_deadline_manager.repository.PeriodicTaskRepository
import com.example.app_deadline_manager.repository.TaskForScheduleRepository
import com.example.app_deadline_manager.repository.TaskRepository
import java.time.LocalDate
import java.time.LocalTime

class ScheduleUseCase(
    private val taskForScheduleRepository: TaskForScheduleRepository,
    private val periodicTaskRepository: PeriodicTaskRepository,
    private val taskRepository: TaskRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getScheduleForDate(date: LocalDate): List<TaskForScheduleModel> {
        val existingTasks = taskForScheduleRepository.findByDate(date) // Проверяем, есть ли уже задачи
        if (existingTasks.isNotEmpty()) {
            return existingTasks // Если задачи есть, просто возвращаем их
        }

        val dayOfWeek = date.dayOfWeek
        val periodicTasks = periodicTaskRepository.findByDayOfWeek(dayOfWeek)

        val taskForScheduleList = mutableListOf<TaskForScheduleModel>()
        for (periodicTask in periodicTasks) {
            for (workPeriod in periodicTask.workPeriods) {
                if (workPeriod.day == dayOfWeek) {
                    taskForScheduleList.add(
                        TaskForScheduleModel(
                            id = taskForScheduleRepository.getNextId(), // Теперь ID всегда уникальный
                            taskId = null,
                            periodicTaskId = periodicTask.id,
                            workStart = workPeriod.startTime,
                            workEnd = workPeriod.endTime,
                            date = date
                        )
                    )
                }
            }
        }

         taskForScheduleRepository.saveAll(taskForScheduleList)

        return taskForScheduleList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createNewTaskForSchedule(
        taskId: Int?,
        workStart: LocalTime,
        workEnd: LocalTime,
        date: LocalDate
    ) {
        val existingTasks = taskForScheduleRepository.findByDate(date)
        for (existingTask in existingTasks) {
            if (overlap(workStart, workEnd, existingTask.workStart, existingTask.workEnd)) {
                throw RuntimeException("Время пересекается с существующей задачей")
            }
        }

        val newTaskForSchedule = TaskForScheduleModel(
            id = taskForScheduleRepository.findAll().size + 1,
            taskId = taskId,
            periodicTaskId = null,
            workStart = workStart,
            workEnd = workEnd,
            date = date
        )

        taskForScheduleRepository.addTaskForSchedule(newTaskForSchedule)
        updateTaskSpentTime(taskId, workStart, workEnd)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun overlap(
        start1: LocalTime,
        end1: LocalTime,
        start2: LocalTime,
        end2: LocalTime
    ): Boolean {
        return (start1.isBefore(end2) && end1.isAfter(start2)) || (start2.isBefore(end1) && end2.isAfter(start1))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskSpentTime(
        taskId: Int?,
        workStart: LocalTime,
        workEnd: LocalTime
    ) {
        val durationInMinutes = java.time.Duration.between(workStart, workEnd).toMinutes().toInt()
        if (taskId != null) {
            val task = taskRepository.findById(taskId)
            task?.let {
                it.spentTime += durationInMinutes
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun removeTask(taskId: Int?) {
        taskForScheduleRepository.removeTaskForSchedule(taskId)
    }
}
