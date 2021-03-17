/*
    This file is part of Team5881_Example.

    Team5881_Example is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Team5881_Example is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.firstinspires.ftc.teamcode;

public class Convert {
    private static final double CM_INCH_RATIO = 2.54;
    static double cmToIn(double input) {
        return input * CM_INCH_RATIO;
    }

    static float cmToIn(float input) {
        return (float) (input * CM_INCH_RATIO);
    }

}
