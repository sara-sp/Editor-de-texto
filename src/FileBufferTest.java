import org.junit.Test;
import java.nio.file.Paths;

import static org.junit.Assert.*;
/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.0
 */
public class FileBufferTest {
    private FileBuffer bf = new FileBuffer();

    /**
     * Testa o Save
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception{
        bf.savePath = Paths.get("/home/sara/tab.txt");

        bf.aux.append("Aasdasdsa pereira 24234");

        bf.save();
        bf.open(Paths.get("/home/sara/tab.txt"));

        System.out.println("@Test SaveAs():" + bf.lineList);
        assertEquals("[Aasdasdsa pereira 24234, ]", bf.lineList.toString());
    }

    /**
     * Testa o SaveAs
     * @throws Exception
     */
    @Test
    public void testSaveAs() throws Exception{
        bf.aux.append("Aasdasdsa pereira 24234\n");

        bf.saveAs(Paths.get("/home/sara/tab.txt"));
        bf.open(Paths.get("/home/sara/tab.txt"));

        System.out.println("@Test SaveAs():" + bf.lineList);
        assertEquals("[Aasdasdsa pereira 24234, ]", bf.lineList.toString());
    }

    /**
     * Testa o Open
     * @throws Exception
     */
    @Test
    public void testOpen() throws Exception{
        bf.open(Paths.get("/home/sara/tab.txt"));

        System.out.println("@Test Open():" + bf.lineList);
        assertEquals("[Aasdasdsa pereira 24234, ]", bf.lineList.toString());

    }
}