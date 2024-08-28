package dm.pagefit.io;

import dm.pagefit.map.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CoordinateFile
{
    public static void saveCoordinates(String filename, List<Coordinate> coordinates)
    {
        List<String[]> strings = new ArrayList();

        for (Coordinate coordinate: coordinates)
        {
            String[] strArr = new String[]{""+coordinate.getX(), ""+coordinate.getY()};
            strings.add(strArr);
        }

        FileIO.writeFileLines(filename, ",", strings);
    }

    public static List<Coordinate> loadCoordinates(String filename)
    {
        List<Coordinate> coordinates = new ArrayList();
        List<String[]> strings = FileIO.getFileLinesSplit(filename, ",");

        for (String[] strArr: strings)
        {
            double x = Double.parseDouble(strArr[0]);
            double y = Double.parseDouble(strArr[1]);
            Coordinate coordinate = new Coordinate(x, y);
            coordinates.add(coordinate);
        }

        return coordinates;
    }
}
