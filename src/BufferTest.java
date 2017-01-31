import org.junit.Test;
import static org.junit.Assert.*;
/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.2
 */
public class BufferTest{
    private Buffer b = new Buffer();

    /**
     * Testa o Insert
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception{
        b.insert('a');
        b.insert('a');
        b.insert('\n');
        b.insert('b');
        b.insert('\n');
        b.insert('c');

        System.out.println("@Test insert(): \t\tlinelist: " + b.lineList);
        assertEquals("[aa, b, c]", b.lineList.toString());
    }

    /**
     * Testa o cursor para a esquerda
     * @throws Exception
     */
    @Test
    public void testmovePrev() throws Exception{                //ver linha deve ser 2 e nao 1
        b.insert('a');
        b.insert('K');
        b.insert('\n');
        b.insert('1');
        b.insert('a');

        b.cursor = new Position(1, 0);
        b.movePrev();

        System.out.println("@Test movePrev(): " + b.cursor.getLinha() + "\t" + b.cursor.getColuna());
        assertEquals(2, b.cursor.getColuna());
        assertEquals(0, b.cursor.getLinha());

    }

    /**
     * Testa o cursor para a direita
     * @throws Exception
     */
    @Test
    public void testmoveNext() throws Exception{
        b.insert('a');
        b.insert('K');
        b.insert('\n');
        b.insert('a');
        b.insert('a');
        b.cursor = new Position(0,2);

        b.moveNext();
        System.out.println("@Test moveNext(): " + b.cursor.getLinha() + "\t" + b.cursor.getColuna());
        assertEquals(0, b.cursor.getColuna());
        assertEquals(1, b.cursor.getLinha());
    }

    /**
     * Testa o cursor para cima
     * @throws Exception
     */
    @Test
    public void testmovePrevLine() throws Exception{
        b.insert('a');
        b.insert('\n');
        b.insert('\n');
        b.insert('K');
        b.insert('a');
        b.cursor = new Position(2,2);

        b.movePrevLine();

        System.out.println("@Test movePrevLine(): " + b.cursor.getLinha() + "\t" + b.cursor.getColuna());
        assertEquals(1, b.cursor.getLinha());
        assertEquals(0, b.cursor.getColuna());
    }

    /**
     * Testa o cursor para baixo
     * @throws Exception
     */
    @Test
    public void testmoveNextLine() throws Exception{
        b.insert('a');
        b.insert('\n');
        b.insert('K');
        b.cursor = new Position(-1,1);

        b.moveNextLine();

        System.out.println("@Test moveNextLine(): " + b.cursor.getLinha() + "\t" + b.cursor.getColuna());
        //assertEquals(1, b.cursor.getLinha());
        //assertEquals(1, b.cursor.getColuna());
    }

    /**
     * Testa NLinhas
     * @throws Exception
     */
    @Test
    public void nlinhas() throws Exception{
        b.insert('b');
        b.insert('\n');
        b.insert('a');

        System.out.println("@Test nlinhas(): " + b.nlinhas());
        assertEquals(2, b.nlinhas());
    }

    /**
     * Testa nesimalinha
     * @throws Exception
     */
    @Test
    public void testenesimalinha() throws Exception{
        b.insert('b');
        b.insert('a');
        b.insert('\n');
        b.insert('b');

        System.out.println("@Test nesimalinha(): " + b.nesimalinha(0));
        assertEquals("ba", b.nesimalinha(0));
    }

    /**
     * Testa validaposicao
     * @throws Exception
     */
    @Test
    public void validaPosicao() throws Exception{
        System.out.println("@Test validaPosicao(): " + b.validaPosicao(new Position(2,0)));
        assertEquals(false, b.validaPosicao(new Position(0,1)));
    }

    //#############################################################################
    //#######                               EXTRAS                        #########
    //#############################################################################

    /**
     * Testa insertAt
     * @throws Exception
     */
    @Test
    public void testInsertAt() throws Exception{
        b.insert('a');
        b.insert('a');
        b.insert('\n');
        b.insert('b');
        b.insert('\n');
        b.insert('c');

        b.insertAt(new Position(0,1),'s');

        System.out.println("@Test insertAt(): " + b.aux + " \t\tlinelist: " + b.lineList);
        assertEquals("asa", b.aux.toString());
    }

    /**
     * Testa apagaLinha
     * @throws Exception
     */
    @Test
    public void testApagaLinha() throws Exception{
        b.insert('a');
        b.insert('a');
        b.insert('\n');
        b.insert('b');
        b.insert('\n');
        b.insert('c');

        b.apagaLinha(new Position(1,0));

        System.out.println("@Test apagaLinha(): " + b.lineList);
        assertEquals("[aa, c]", b.lineList.toString());
    }

    /**
     * Testa deleteAt
     * @throws Exception
     */
    @Test
    public void testDeleteAt() throws Exception{
        b.insert('a');
        b.insert('K');
        b.insert('\n');
        b.insert('b');
        b.insert('l');
        b.insert('\n');
        b.insert('c');
        b.insert('d');

        b.deleteAt(new Position(0,2));
        b.deleteAt(b.cursor);
        b.deleteAt(b.cursor);
        System.out.println("@Test DeleteAt():\t\tlinelist: " + b.lineList);
        //assertEquals("[abl, cd]", b.lineList.toString());
    }

    /**
     * Testa setMark
     * @throws Exception
     */
    @Test
    public void testSetMark() throws Exception{
        b.setMark(0,0);
        System.out.println("@Test setMark():\t\tlinha:" + b.selecLinha + "\t\tcoluna:" + b.selecColuna);
        assertEquals(0, b.selecLinha);
        assertEquals(0, b.selecColuna);
    }

    /**
     * Testa unsetMark
     * @throws Exception
     */
    @Test
    public void testUnsetMark() throws Exception{
        b.unsetMark();

        System.out.println("@Test unsetMark(): " + b.selecLinha + " " + b.selecColuna);
        assertEquals(-1, b.selecLinha);
        assertEquals(-1, b.selecColuna);
    }

    /**
     * Testa copy
     * @throws Exception
     */
    @Test
    public void testCopy() throws Exception{

        String chars = "andre123\npereira\nSara";

        for(int i=0; i<chars.length();i++)
            b.insert(chars.charAt(i));

        b.copy(new Position(0,2), new Position(2,2));

        System.out.println(b.lineList);
        System.out.println(b.linelist2);

        b.cursor.setLinha(0); //linha onde se quer colar;
        b.cursor.setColuna(5); //coluna onde se quer colar

        b.paste();

        System.out.println("@Test copy(): " + b.lineList);
        assertEquals("[andredre123123, pereira, Sar, pereira, Sara]", b.lineList.toString());
    }

    /**
     * Testa cut
     * @throws Exception
     */
    @Test
    public void testCut() throws Exception{
        String chars = "andre123\npereira\nSara";

        for(int i=0; i<chars.length();i++)
            b.insert(chars.charAt(i));

        b.cut(new Position(0,2), new Position(2,3));
        b.cursor.setLinha(0);
        b.cursor.setColuna(1);
        b.paste();

        System.out.println("@Test cut(): " + b.lineList);
       // assertEquals("[a, dre123, pereira, Saran]", b.lineList.toString());
    }

    /**
     * Testa paste
     * @throws Exception
     */
    @Test
    public void testPaste() throws Exception{
        b.linelist2.add(0, new StringBuilder("sara"));
        b.paste();

        System.out.println("@Test paste(): " + b.lineList);
        assertEquals("[sara]", b.lineList.toString());
    }

    /**
     * Teste Undo
     * @throws Exception
     */
    @Test
    public void testUndo() throws Exception{

        b.insert('a');
        b.insert('d');
        b.undo();

        System.out.println("@Test Undo(): " + b.lineList);
        //assertEquals("[sara]", b.lineList.toString());
    }
}