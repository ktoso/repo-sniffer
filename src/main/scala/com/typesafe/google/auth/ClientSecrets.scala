package com.typesafe.google.auth

import java.io.{InputStreamReader, FileReader}

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.json.jackson2.JacksonFactory

object ClientSecrets {
  val ClientsecretsLocation = "/client_secrets.json"

  lazy val ClientSecrets = loadClientSecrets()

  def loadClientSecrets(): GoogleClientSecrets = {
    try {
      GoogleClientSecrets.load(new JacksonFactory(), new InputStreamReader(getClass.getResourceAsStream(ClientsecretsLocation)))
    } catch {
      case e: Exception =>
        System.err.println("Could not load client_secrets.json")
        e.printStackTrace()

        System.exit(1)
        null
    }
  }
}
