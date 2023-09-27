package com.timserio.home_data.location

import android.app.Application
import android.location.Geocoder
import android.os.Build
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class GeocodeLocationImpl @Inject constructor(
    private val app: Application
) : GeocodeLocation {
    override fun geocodeLocation(location: Pair<Double, Double>, callback: (LocationData) -> Unit) {
        val geocoder = Geocoder(app, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(location.first, location.second, 1) {
                callback(
                    LocationData(
                        location.first,
                        location.second,
                        it[0].adminArea,
                        it[0].locality
                    )
                )
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.first, location.second, 1)
                val address = addresses?.get(0)
                callback(
                    LocationData(
                        location.first,
                        location.second,
                        address?.adminArea,
                        address?.locality
                    )
                )
            }
        }
    }
}
