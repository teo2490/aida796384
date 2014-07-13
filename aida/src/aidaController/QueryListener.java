package aidaController;

import exception.InvalidSequentialPatternException;


/*** 
 * The interface for a query listener.
 *
 * Copyright (c) 2014 Matteo Simoni 796384
 * Mail to matteo.simoni@mail.polimi.it
 * 
 * This file is part of the AIDA SOFTWARE
 * (https://code.google.com/p/aida796384/).
 *
 * AIDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AIDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AIDA.  If not, see <http://www.gnu.org/licenses/>.
 */

public interface QueryListener {
	public void someoneMadeQuery(int q) throws InvalidSequentialPatternException, InterruptedException;
}
