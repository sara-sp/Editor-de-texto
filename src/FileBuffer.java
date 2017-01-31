import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.io.*;
/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.0
 */
public class FileBuffer extends Buffer {
    Path savePath;          // null= não definido
    boolean modified;       // true= modificado; false= inalterado;
    Buffer bf;

    FileBuffer() {
        modified = false;
        bf = new Buffer();
    }

    /**
     * gravar
     * @throws IOException
     */
    public void save() throws IOException {
        FileWriter fw = new FileWriter(savePath.toFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(aux.toString());
        bw.close();
    }

    /**
     * gravar como
     * @param path              ficheiro onde pretende gravar
     * @throws IOException
     */
    public void saveAs(Path path) throws IOException {
        File ficheiro = new File(path.toString());

        if (!ficheiro.exists())
            ficheiro.createNewFile();

        FileWriter fw = new FileWriter(ficheiro.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(aux.toString());
        bw.close();
    }

    /**
     * abrir
     * @param path              ficheiro onde pretende gravar
     * @throws IOException
     */
    public void open(Path path) throws IOException {
        Scanner inp = new Scanner(new FileReader(path.toString()));
        aux = new StringBuilder();

        while (inp.hasNextLine()) {
            aux.append(inp.nextLine());
            lineList.add(cursor.getLinha(), aux);
            aux = new StringBuilder();
            cursor.setLinha(cursor.getLinha() + 1);
        }

        inp.close();
    }

    @Override
    public void insert(char c) {
        super.insert(c);
        modified = true; // marcar modificação
    }

    @Override
    public void insertAt(Position cursor, char c) {
        super.insertAt(cursor, c);
        modified = true; // marcar modificação
    }

    @Override
    public void apagaLinha(Position cursor) {
        super.apagaLinha(cursor);
        modified = true; // marcar modificação
    }

    @Override
    public void deleteAt(Position cursor) {
        super.deleteAt(cursor);
        modified = true; // marcar modificação
    }

    @Override
    public void copy(Position p1, Position p2) {
        super.copy(p1, p2);
        modified = true; // marcar modificação
    }

    @Override
    public void cut(Position p1, Position p2) {
        super.cut(p1, p2);
        modified = true; // marcar modificação
    }

    @Override
    public void paste() {
        super.paste();
        modified = true; // marcar modificação
    }

    @Override
    public void undo(){
        super.undo();
        modified = true; // marcar modificação
    }
}