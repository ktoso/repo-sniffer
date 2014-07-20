package com.typesafe.google.auth

import java.io.{BufferedReader, File, InputStreamReader}
import java.util

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleAuthorizationCodeRequestUrl}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.bigquery.BigqueryScopes


object GAuth {

  val Scopes = util.Arrays.asList(BigqueryScopes.BIGQUERY)

  val httpTransport = new NetHttpTransport()
  val jsonFactory = new JacksonFactory()

  val redirectUrl = "http://localhost"

  lazy val authFlow = {
    new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, ClientSecrets.ClientSecrets, Scopes)
      .setCredentialDataStore(new FileDataStoreFactory(new File(".")).getDataStore("bigquery"))
      .setAccessType("offline")
      .setApprovalPrompt("force")
      .build()
  }

  lazy val existingCredential = Option(authFlow.loadCredential("bigquery"))

  def auth(): Credential = existingCredential match {
    case Some(credential) =>
      credential

    case _ =>
      val authorizeUrl =
        new GoogleAuthorizationCodeRequestUrl(ClientSecrets.ClientSecrets, redirectUrl, Scopes).setState("").build()

      println("Paste this URL into a web browser to authorize BigQuery Access:\n" + authorizeUrl)
      println("... and type the code you received here: ")
      val in = new BufferedReader(new InputStreamReader(System.in))
      val authorizationCode = in.readLine()

      val response = authFlow.newTokenRequest(authorizationCode).setRedirectUri(redirectUrl).execute()
      authFlow.createAndStoreCredential(response, "bigquery")
  }

}
