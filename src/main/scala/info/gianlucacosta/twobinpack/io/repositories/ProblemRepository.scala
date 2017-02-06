/*^
  ===========================================================================
  TwoBinKernel
  ===========================================================================
  Copyright (C) 2016-2017 Gianluca Costa
  ===========================================================================
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  ===========================================================================
*/

package info.gianlucacosta.twobinpack.io.repositories

import java.util.UUID

import info.gianlucacosta.twobinpack.core.Problem

/**
  * Repository simplifying common storage operations on Problem instances
  */
trait ProblemRepository {
  /**
    * Returns all the problem names, sorted
    *
    * @return
    */
  def findAllNamesSorted(): Iterable[String]

  /**
    * Finds a problem given its ID
    *
    * @param id The problem ID
    * @return The problem - or None, if it was not found
    */
  def findById(id: UUID): Option[Problem]

  /**
    * Finds a problem given its name
    *
    * @param name The problem name
    * @return The problem - or None, if it was not found
    */
  def findByName(name: String): Option[Problem]

  /**
    * Adds a new problem to the repository
    *
    * @param problem The problem to add. It must not be in the repository
    */
  def add(problem: Problem): Unit

  /**
    * Updates an existing problem
    *
    * @param problem The problem to update. A previous copy must already be in the repository
    */
  def update(problem: Problem): Unit

  /**
    * Removes the problem having the given name
    *
    * @param name Name of the problem to remove
    */
  def removeByName(name: String): Unit


  /**
    * Removes all the problems from the repository
    */
  def removeAll()


  /**
    * Total count of problems in the repository
    *
    * @return
    */
  def count(): Long
}
