/**
 * Custom API for home automation
 *
 * Author: cramptonism
 */

definition(
  name: "SmartThings Custom API",
  namespace: "cramptonism",
  author: "Andrew Crampton",
  description: "Custom API for home automation",
  category: "Convenience",
  iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
  iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
  iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
  oauth: true)

preferences {
  section("Presence") {
    input("presenceSensors", "capability.presenceSensor", title: "Presence Sensors", required: true, multiple: true)
  }
  section("Climate") {
    input("upstairsSensors", "capability.temperatureMeasurement", title: "Upstairs Sensors", required: true, multiple: true)
    input("downstairsSensors", "capability.temperatureMeasurement", title: "Downstairs Sensors", required: true, multiple: true)
  }
}

mappings {
  path("/summary") {
    action: [
      GET: "getSummary"
    ]
  }
}

def installed() {}

def updated() {}

def getSummary() {
  [
    "mode": location.mode,
    "presence": getPresence(),
    "climate": getClimate()
  ]
}

def getPresence() {
  presenceSensors.collectEntries {[it.label, it.currentPresence]}
}

def getClimate() {
  def upstairsTemperature
  if (upstairsSensors != null && upstairsSensors.size() > 0) {
    float upstairsTotal = 0.0
    upstairsSensors.each {
      upstairsTotal += it.currentTemperature
    }
    upstairsTemperature = Math.round(upstairsTotal / upstairsSensors.size())
  }

  def downstairsTemperature
  if (downstairsSensors != null && downstairsSensors.size() > 0) {
    float downstairsTotal = 0.0
    downstairsSensors.each {
      downstairsTotal += it.currentTemperature
    }
    downstairsTemperature = Math.round(downstairsTotal / downstairsSensors.size())
  }

  [
    "upstairsTemperature": upstairsTemperature,
    "downstairsTemperature": downstairsTemperature
  ]
}
