import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class DataLayerListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path?.compareTo("/sync_request") == 0) {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val accountEmail = dataMap.getString("accountEmail")
                    // Handle the account sync request
                    // For example, update the email in shared preferences or start an activity
                }
            }
        }
    }
}
