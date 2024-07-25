import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.JsonFileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class EventViewModel : ViewModel() {
    private val eventFile = File("events.json")
    private var events = mutableListOf<Event>()

    init {
        events = JsonFileUtil.readFromFile(eventFile).toMutableList()
    }

    private fun saveEvents() {
        JsonFileUtil.writeToFile(eventFile, events)
    }

    fun addEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            events.add(event)
            saveEvents()
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            events.remove(event)
            saveEvents()
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            val index = events.indexOfFirst { it.id == event.id }
            if (index != -1) {
                events[index] = event
                saveEvents()
            }
        }
    }

    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }

    fun getAllEvents(): List<Event> {
        return events
    }
}
