package com.example.util;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Converter class, which was used to convert the given
 * sector selectbox options from HTML to SQL format.
 *
 * <p>Firstly, the data is read into memory from a file. It
 * is assumed that the file contains only the convertable
 * &lt;option /&gt; elements.
 * <p>Then the program calculates the hierarchy references
 * for the in-memory hierarchy.
 * <p>Finally the hierarchy is saved to an SQL file.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 21/07/16
 */
public class SectorsHtmlToSql {

    public static final String ID_START = "<option value=\"";
    public static final String ID_END = "\"";
    public static final String NAME_START = ">";
    public static final String NAME_END = "<";
    public static final String INDENTATION = "&nbsp;&nbsp;&nbsp;&nbsp;";
    public static final String SQL_FORMAT = "insert into sector (id, name, parent) values (%s, \'%s\', %s);\n";
    public static final String ROOT_SECTOR_SQL = "insert into sector (id, name, parent) values (0, 'Root sector', null);\n";

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        new SectorsHtmlToSql().convert("src/main/resources/sectors.xml", "src/test/resources/sql/sectors.sql");
    }

    public void convert(String source, String target) throws IOException {
        List<Sector> sectors = readData(source);
        calculateParents(sectors);
        exportSectorsSql(sectors, target);
    }

    private void exportSectorsSql(List<Sector> sectors, String destinationPath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(destinationPath))) {
            writer.write(ROOT_SECTOR_SQL);
            for (Sector sector : sectors) {
                writer.write(String.format(SQL_FORMAT, sector.getId(), sector.getName(), sector.getParent()));
            }
        }
    }

    private void calculateParents(List<Sector> sectors) {
        Stack<Long> parents = new Stack<>();
        parents.push(0L);
        parents.push(0L);

        int indentationDepth = 0;
        for (Sector sector : sectors) {
            int nameIndentationDepth = getIndentationDepth(sector.getName());
            sector.setName(normalizeSectorName(sector.getName()));

            if (nameIndentationDepth > indentationDepth) {
                sector.setParent(parents.peek());
            } else if (nameIndentationDepth == indentationDepth) {
                parents.pop();
                if (!parents.isEmpty()) {
                    sector.setParent(parents.peek());
                }
            } else {
                for (int i = 0; i < indentationDepth - nameIndentationDepth + 1; i++) {
                    parents.pop();
                }

                sector.setParent(parents.peek());
            }

            indentationDepth = nameIndentationDepth;
            parents.push(sector.getId());
        }
    }

    private String normalizeSectorName(String name) {
        return name.replace(INDENTATION, "");
    }

    private int getIndentationDepth(String name) {
        int indentationDepth = 0;
        while (true) {
            if (name.contains(INDENTATION)) {
                name = name.substring(INDENTATION.length());
                indentationDepth++;
            } else {
                break;
            }
        }

        return indentationDepth;
    }

    private List<Sector> readData(String fileName) {
        List<Sector> sectors = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                Long id = parseId(line);
                String name = parseName(line);
                sectors.add(new Sector(id, 0L, name.trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sectors;
    }

    private String parseName(String line) {
        String name = line.substring(line.indexOf(NAME_START) + 1);
        return name.substring(0, name.indexOf(NAME_END));
    }

    private Long parseId(String line) {
        String id = line.substring(ID_START.length());
        return Long.valueOf(id.substring(0, id.indexOf(ID_END)));
    }


    class Sector {
        Long id;
        Long parent;
        String name;

        public Sector(Long id, Long parent, String name) {
            this.id = id;
            this.parent = parent;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public Long getParent() {
            return parent;
        }

        public void setParent(Long parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
