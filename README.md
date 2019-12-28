Alarmmonitor is a simple alarm monitor that can display alarms for fire departments. It is controlled by a REST controller.

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
   
   Alarm structure:
   
   {
  "alarmedCars": [
    "carName",
    "carName"
  ],
  "keyword": {
    "name": "Brandeinsatz",
    "stage": 1
  },
  "address": {
    "zipCode": 76133,
    "street": "Kaiserstraße",
    "number": 121,
    "location": "Deutschland"
  }
}

Configure the application: there is a configuration folder, containing two files:
cars.json and applicationConfiguration.json.

Insert your cars in cars.json into the given structure and watch out for correct and unique IDs. FMS represents the initial FMS state of the car.
The applicationConfiguration.json contains field for the map center and the home coordinates of the fire departement.

Alarmmonitor shows a neutral panel with a smooth image show and a calendar of upcoming events.
Images are located in userhome/alarmmonitor/images

Planned improvements:

* implement alarms by coordinates for direct and automated interaction with control centers
* direct PDF export of routing instructions
* CSV import for calendar

Feel free to open issues and feature requests or contact me directly: privat@sebastianschwegler.de
