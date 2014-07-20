package com.typesafe.google.bigquery

object SQLs {

  def allScalaProjects: String = {
    """
      SELECT repository_created_at, repository_name, type
      FROM [githubarchive:github.timeline]
      WHERE repository_language LIKE 'Scala'
    """
  }
}
