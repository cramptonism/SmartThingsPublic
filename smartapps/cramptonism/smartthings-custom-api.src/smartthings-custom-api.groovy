/**
 * Custom API for home automation
 *
 * Author: cramptonism
 */

definition(
  name: "Black Nugget Window Monitor",
  namespace: "cramptonism",
  author: "Andrew Crampton",
  description: "Monitors outside versus inside temperatures and sends notifications if windows should be opened or closed.",
  category: "Convenience",
  iconUrl: "http://truello.com/smart/static/window.png",
  iconX2Url: "http://truello.com/smart/static/window@2x.png",
  iconX3Url: "http://truello.com/smart/static/window@3x.png",
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
  path("/notify/:recipients/:message") {
    action: [
      GET: "sendNotifications"
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

def sendNotifications() {
  params.recipients.tokenize(",").each {
    sendSms(it, params.message)
  }
}
