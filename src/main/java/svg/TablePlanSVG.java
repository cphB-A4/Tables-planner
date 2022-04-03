package svg;

import dtos.BigEventDTO;
import dtos.PersonDTO;
import dtos.TablesDTO;
import entities.Tables;

import java.util.List;

public class TablePlanSVG extends SVG {

    StringBuilder svg = new StringBuilder();
    List<TablesDTO> tableList;

    public TablePlanSVG(int x, int y, String viewBox, int width, int height, BigEventDTO bigEventDTO) {
        //Starterkode til SVG
        svg.append(String.format(headerTemplate, height, width, viewBox, x, y));
        tableList = bigEventDTO.getTablesList();
    }

    public void generateTable(){
        fourPeopleTable();
    }

    public void fourPeopleTable(){
        //4 people table

        addRect(50,50,100.0,150.0,svg);
        addCircle(80,30,15,svg);
        addCircle(170,30,15,svg);
        addCircle(170,170,15,svg);
        addCircle(80,170,15,svg);
    }

    public void fourPeopleTableLine(){
        //4 people table
        double tableHeight = 100.0;
        double tableWidth = 150.0;

        //

        //test 5 tables:
        int multiplyFactor = 200;
        int rowNumber = 0;
        int columnNumber = 0;
        int i = 0;
        String name = null;
        for (TablesDTO tablesDTO: tableList) {

            int newI = i-(5*rowNumber);
            addRect(newI*multiplyFactor,200 * rowNumber,tableHeight,tableWidth,svg);



            //2 seats
            if (tablesDTO.getSize() > 2) {
                addLine(75 + (multiplyFactor * newI), (0 + 200) * rowNumber, 75 + (multiplyFactor * newI), (200 * rowNumber) + 100, svg);
                String [] names = new String[tablesDTO.getSize()];
                for (int j = 0; j < tablesDTO.getSize(); j++){
                    if(tablesDTO.getPersons().size() > j) {
                        //display only 4 characters
                        if (tablesDTO.getPersons().get(j).getName().length() > 4){
                            name = tablesDTO.getPersons().get(j).getName().substring(0,4);
                        } else {
                            name = tablesDTO.getPersons().get(j).getName();
                        }

                        names[j] = name;
                    } else{
                        //if persons are not provided
                        names[j] = "???";
                    }
                }

                addText((newI * multiplyFactor) + 10, (200 * rowNumber) + 30, names[0], svg);
                addText((newI * multiplyFactor) + 10, (200 * rowNumber) + 80, names[2], svg);
                addText((newI * multiplyFactor) + 85, (200 * rowNumber) + 30, names[1], svg);
                addText((newI * multiplyFactor) + 85, (200 * rowNumber) + 80, names[3], svg);

            } else {
                String[] names = new String[tablesDTO.getSize()];
                for (int j = 0; j < tablesDTO.getSize(); j++) {
                    if (tablesDTO.getPersons().size() > j) {
                        //display only 4 characters
                        if (tablesDTO.getPersons().get(j).getName().length() > 4) {
                            name = tablesDTO.getPersons().get(j).getName().substring(0, 4);
                        } else {
                            name = tablesDTO.getPersons().get(j).getName();
                        }

                        names[j] = name;
                    } else {
                        //if persons are not provided
                        names[j] = "???";
                    }
                }
                addText((newI * multiplyFactor) + 50, (200 * rowNumber) + 30, names[0], svg);
                addText((newI * multiplyFactor) + 50, (200 * rowNumber) + 80, names[1], svg);
            }
            addLine((multiplyFactor*newI),(200*rowNumber) + 50,150 + (multiplyFactor*newI), (rowNumber* 200 + 50),svg);
            columnNumber++;
            if (columnNumber % 5 == 0){
                rowNumber++;
            }
            i++;
        }
    }
    public String genarateSvg(){
        //generateTable();
        fourPeopleTableLine();
        return svg + "</svg>";
    }

}