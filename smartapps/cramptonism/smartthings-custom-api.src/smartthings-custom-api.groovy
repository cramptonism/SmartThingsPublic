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
  section("Climate") {
    input("upstairsSensors", "capability.temperatureMeasurement", title: "Upstairs Sensors", multiple: true)
    input("downstairsSensors", "capability.temperatureMeasurement", title: "Downstairs Sensors", multiple: true)
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
    "climate": getClimate()
  ]
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
