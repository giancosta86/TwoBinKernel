/*^
  ===========================================================================
  TwoBinKernel
  ===========================================================================
  Copyright (C) 2016 Gianluca Costa
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

import info.gianlucacosta.twobinpack.core.Solution

/**
  * Repository simplifying common storage operations on Solution instances
  */
trait SolutionRepository {
  /**
    * Returns all the solutions in the repository
    *
    * @return
    */
  def findAll(): Iterable[Solution]

  /**
    * Returns all the solutions related to the given problem
    *
    * @param problemName The problem name. It can be a name not in the repository
    * @return The solutions to the given problem, or an empty iterable if the problem is missing
    */
  def findAllByProblemName(problemName: String): Iterable[Solution]

  /**
    * Finds the solution having the given id
    *
    * @param id The solution id
    * @return The solution, or None - if it was not found
    */
  def findById(id: UUID): Option[Solution]

  /**
    * Add a solution to the repository
    *
    * @param solution The solution to add. It must not be in the repository
    */
  def add(solution: Solution): Unit


  /**
    * Removes all the solutions related to the given problem; if such problem is not
    * in the repository, nothing happens
    *
    * @param problemName The problem name. It can be a name not in the repository
    */
  def removeByProblemName(problemName: String)

  /**
    * Total count of solutions in the repository
    *
    * @return
    */
  def count(): Long
}
