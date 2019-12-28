Alarmmonitor offers the following endpoints:

* POST /alarm/dispatch:
    This endpoint accepts the alarm that should be displayed. See alarm object structure for information about the 
    JSON structure
    Returns: 200 after 60 seconds
* GET /fms/change/car/{carName}/id/{carId}/state/{state}
    Updates the given cars FMS state
    Returns: 200 if the update was successful
* GET /car/{carName}/changeposition/lat/{lat}/long/{longitude}
   Experimental function to display car positions on the alarm map on their way enroute to the scene
   Returns: 200 always
