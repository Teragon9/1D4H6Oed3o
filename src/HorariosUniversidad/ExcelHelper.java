package HorariosUniversidad;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ExcelHelper {

    public void generarHorariosProfesores(Clase[] clases, Horario horario) {
        String asignatura, aula, profesor, tiempo, grupo;
        int idGrupo, idAula, idProfesor;
        XSSFWorkbook workbook = new XSSFWorkbook();
        HashMap<Integer, XSSFSheet> sheets = new HashMap<>();
        for (Clase clase : clases) {
            idProfesor = clase.getIdProfesor();
            profesor = horario.getProfesor(idProfesor).getNomPorfesor();
            if (!sheets.containsKey(clase.getIdProfesor())) {
                XSSFSheet sheet = workbook.createSheet(horario.getProfesor(clase.getIdProfesor()).getNomPorfesor());
                createSheet(workbook, sheet, profesor);
                sheets.put(clase.getIdProfesor(), sheet);
            }
            XSSFSheet sheet = sheets.get(clase.getIdProfesor());
            aula = horario.getAula(clase.getIdAula()).getNombreAula();
            tiempo = horario.getTiempos(clase.getIdTiempo()).getEspacioTiempo();
            asignatura = horario.getAsignatura(clase.getIdAsignatura()).getAsignatura();
            agregarHoras(aula, tiempo, asignatura, workbook, sheet);
        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("GENERADO " + fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void agregarHoras(String aula, String espacioTiempo, String asignatura, XSSFWorkbook workbook, XSSFSheet sheet) {
        String dia = espacioTiempo.substring(0, 3);
        StringTokenizer st = new StringTokenizer(espacioTiempo.substring(4), "- ");
        String horaInicio = st.nextToken();
        String horaFin = st.nextToken();
        st = new StringTokenizer(horaInicio, ":");
        int inicio = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(horaFin, ":");
        int fin = Integer.parseInt(st.nextToken());
        int columna = 6;
        switch (dia) {
            case "Lun":
                columna = 1;
                break;
            case "Mar":
                columna = 2;
                break;
            case "Mie":
                columna = 3;
                break;
            case "Jue":
                columna = 4;
                break;
            case "Vie":
                columna = 5;
                break;
        }

        for (int fila = inicio; fila < fin; fila++) {
            XSSFRow row = sheet.getRow(fila - 5);
//            System.out.println(row.getCell(0).getStringCellValue());
//            System.out.println(asignatura+"\n"+aula);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            XSSFCell cell = row.createCell(columna);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(asignatura + "\n" + aula);
        }

    }


    public static void main(String[] args) {
        String a, b;
        a = "Lun 7:00- 10:00";
        System.out.println((a.substring(0, 3)));
        StringTokenizer st = new StringTokenizer(a.substring(4), "- ");
        b = st.nextToken();
        System.out.println(b);
        System.out.println(st.nextToken());
    }

    public void createSheet(XSSFWorkbook workbook, XSSFSheet sheet, String nombreProf) {

        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);

        XSSFRow profesorRow = sheet.createRow(0);
        XSSFCell profesorRowCell = profesorRow.createCell(3);
        profesorRowCell.setCellValue(nombreProf);

        XSSFRow rowHeaders = sheet.createRow(1);
        rowHeaders.createCell(0).setCellValue("");
        rowHeaders.createCell(1).setCellValue("Lunes");
        rowHeaders.createCell(2).setCellValue("Martes");
        rowHeaders.createCell(3).setCellValue("Miercoles");
        rowHeaders.createCell(4).setCellValue("Jueves");
        rowHeaders.createCell(5).setCellValue("Viernes");
        rowHeaders.createCell(6).setCellValue("Sabado");

        XSSFCellStyle profesorStyle = workbook.createCellStyle();
        profesorStyle.setWrapText(true);
        XSSFFont profesorFont = workbook.createFont();
        profesorFont.setBold(true);
        profesorFont.setFontHeightInPoints((short) 16);
        profesorFont.setFontName("Arial");
        profesorStyle.setFont(profesorFont);
        profesorRowCell.setCellStyle(profesorStyle);

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
        rowHeaders.getCell(0).setCellStyle(headerStyle);
        rowHeaders.getCell(1).setCellStyle(headerStyle);
        rowHeaders.getCell(2).setCellStyle(headerStyle);
        rowHeaders.getCell(3).setCellStyle(headerStyle);
        rowHeaders.getCell(4).setCellStyle(headerStyle);
        rowHeaders.getCell(5).setCellStyle(headerStyle);
        rowHeaders.getCell(6).setCellStyle(headerStyle);

        for (int i = 0; i < 12; i++) {
            XSSFRow row = sheet.createRow(i + 2);
            row.setHeightInPoints((short) 48);
            XSSFCell cell = row.createCell(0);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            cell.setCellStyle(cellStyle);
            if (i + 7 < 9)
                cell.setCellValue("0" + (i + 7) + ":00\n0" + (i + 8) + ":00");
            else if (i + 7 == 9)
                cell.setCellValue("0" + (i + 7) + ":00\n" + (i + 8) + ":00");
            else
                cell.setCellValue((i + 7) + ":00\n" + (i + 8) + ":00");
            for (int j = 1; j < 6; j++) {
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(" ");
            }
        }
    }


}
