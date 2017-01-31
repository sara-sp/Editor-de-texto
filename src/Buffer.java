import java.util.*;

/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.2
 */
public class Buffer{

    List<StringBuilder> lineList = new ArrayList<>();
    List<StringBuilder> linelist2 = new ArrayList<>();
    private ArrayList<Edit> undoList;
    StringBuilder aux;
    //StringBuilder clipBoard;
    Position cursor;
    int selecLinha=-1, selecColuna=-1;

    /**
     * Metodo construtor da classe responsavel pela visao logica do editor de texto
     */
    public Buffer(){
        cursor=new Position(0,0);
        aux = new StringBuilder("");
        lineList.add(0, new StringBuilder(""));
        undoList = new ArrayList<>();
        selecLinha = 0;
        selecColuna = 0;
    }

    /**
     * inserir carater
     *
     * @param c        caracter a inserir
     */
    public void insert(char c){
        if(c=='\n')
            insereLinha();

        else{
            aux = lineList.get(cursor.getLinha());
            aux.insert(cursor.getColuna(), c);
            moveNext();                             //direita
            lineList.set(cursor.getLinha(), aux);   //atualiza lineList
        }
    }

    /**
     * inserir linha
     */
    public void insereLinha(){
        lineList.add(cursor.getLinha(), aux);
        moveNextLine();                             //nova linha (baixo)
        cursor.setColuna(0);
        aux = new StringBuilder("");
        lineList.set(cursor.getLinha(), aux);       //atualiza lineList
    }

    /**
     * mover o cursor esquerda
     */
    void movePrev(){
        if(validaPosicao(cursor)){
            if(cursor.getColuna()>0)
                cursor.setColuna(cursor.getColuna() - 1);
            else if(cursor.getLinha()>0){                                       //caso esteja no inicio da linha
                cursor.setLinha(cursor.getLinha()-1);
                cursor.setColuna(nesimalinha(cursor.getLinha()).length());
            }
        }
        else
            convertePos(cursor);
    }

    /**
     * mover o cursor direita
     */
    void moveNext(){
        if(validaPosicao(cursor)){
            if (nesimalinha(cursor.getLinha()).length() > cursor.getColuna())
                cursor.setColuna(cursor.getColuna() + 1);

            else if (cursor.getLinha() + 1 < nlinhas()){
                cursor.setLinha(cursor.getLinha() + 1);
                cursor.setColuna(0);//   cursor.setColuna(nesimalinha(cursor.getLinha()).length());
            }
        }
        else
            convertePos(cursor);

   }

    /**
     * mover o cursor cima/linha anterior
     */
    void movePrevLine(){
        if(validaPosicao(cursor)) {
            if(cursor.getLinha() > 0){
                cursor.setLinha(cursor.getLinha() - 1);
                cursor.setColuna(Math.min(cursor.getColuna(), nesimalinha(cursor.getLinha()).length()));
            }
        }

        else
            convertePos(cursor);

    }

    /**
     * mover o cursor baixo/proxima linha
     */
    void moveNextLine(){
       if(validaPosicao(cursor)){
            if(cursor.getLinha()!=lineList.size()-1)
                cursor.setLinha(cursor.getLinha() + 1);
            cursor.setColuna(Math.min(cursor.getColuna(), nesimalinha(cursor.getLinha()).length()));
       }
       else
            convertePos(cursor);
    }

    /**
     * retorna numero de linha
     */
    int nlinhas(){
        return lineList.size();
    }

    /**
     * conteudo de certa linha
     *
     * @param nlinha    linha selecionada
     * @return          retorna uma String com o conteudo da linha pretendida
     */
    String nesimalinha(int nlinha){
        return lineList.get(nlinha).toString();
    }

    /**
     * valida a posicao dentro das possiveis margens
     *
     * @param cursor    posição atual
     * @return          retorna verdade se a posicao esta dentro das margens e falso caso contrario
     */
    public boolean validaPosicao(Position cursor){
        return (cursor.getLinha()>=0 && cursor.getColuna()>=0 && cursor.getLinha()<nlinhas() && cursor.getColuna()<=nesimalinha(cursor.getLinha()).length());
    }

    public void convertePos(Position cursor){
        if (cursor.getLinha() <= 0 && cursor.getColuna() <= 0) {
            cursor.setLinha(0);
            cursor.setColuna(0);
        }
        else if (cursor.getColuna() <= 0) {
            cursor.setColuna(0);
            if (cursor.getLinha()>lineList.size())
                cursor.setLinha(lineList.size() - 1);
        }
        else if (cursor.getLinha() <= 0) {
            cursor.setLinha(0);
            if (!validaPosicao(cursor))
                cursor.setColuna(nesimalinha(cursor.getLinha()).length());
        }
        else{
            cursor.setLinha(lineList.size() - 1);
            cursor.setColuna(nesimalinha(cursor.getLinha()).length());
        }
    }


    //#############################################################################
    //#######                               EXTRAS                        #########
    //#############################################################################

    /**
     * inserir caracter a seguir a determinada posicao
     *
     * @param cursor   posicao atual
     * @param c        caracter a inserir
     */
    public void insertAt(Position cursor,char c){
        this.cursor=cursor;
        if(validaPosicao(cursor)){
            if(c=='\n')
                insereLinha();

            else{
                aux = lineList.get(cursor.getLinha());
                aux.insert(cursor.getColuna(), c);
                moveNext();    //direita
                lineList.set(cursor.getLinha(), aux);   //atualiza lineList
            }
        }
    }

    /**
     * apaga linha anterior a determinada posicao
     *
     * @param cursor   posicao atual
     */
    public void apagaLinha(Position cursor){
        this.cursor=cursor;
        if(validaPosicao(cursor))
            lineList.remove(cursor.getLinha());
    }

    /**
     * apaga caracter anterior a determinada posicao
     *
     * @param cursor   posicao atual
     */
    public void deleteAt(Position cursor){
        this.cursor=cursor;
        int tamAnt;

        if(validaPosicao(cursor)){
            if(cursor.getColuna()<=0 && cursor.getLinha()<=0)
                return;

            if(cursor.getColuna()>0 ) {
                aux = lineList.get(cursor.getLinha());
                aux.deleteCharAt(cursor.getColuna()-1);
                movePrev();
                lineList.set(cursor.getLinha(), aux);   //atualiza lineList
            }

            else if(cursor.getColuna()==0){
                tamAnt = nesimalinha(cursor.getLinha()-1).length();
                StringBuilder apaga= new StringBuilder(nesimalinha(cursor.getLinha()-1));
                apaga.append(nesimalinha(cursor.getLinha()));
                lineList.set(cursor.getLinha()-1, apaga);
                cursor.setColuna(tamAnt-1);
                apaga.deleteCharAt(tamAnt-1);
                movePrev();
                lineList.set(cursor.getLinha(), aux);
                lineList.remove(cursor.getLinha());
                movePrevLine();
                cursor.setColuna(nesimalinha(cursor.getLinha()).length());
            }
        }
    }


    /**
     * seleciona o pretendido
     *
     * @param linha   linha a marcar
     * @param coluna  coluna a marcar
     */
    public void setMark(int linha, int coluna){
        if(validaPosicao(new Position(linha,coluna))){
            this.selecLinha = linha;
            this.selecColuna = coluna;
        }
    }

    /**
     * seleciona o pretendido
     */
    public void setMark(){
       setMark(cursor.getLinha(), cursor.getColuna());
    }

    /**
     * desseleciona o anteriormente selecionado
     */
    public void unsetMark(){
        this.selecLinha=-1;
        this.selecColuna=-1;
    }

    /**
     * Remove o que foi selecionado/copiado anteriormente
     * @param p1        posicao inicial
     * @param p2        posicao final
     */
    public void removeMarcado(Position p1, Position p2) {
        for (int i = nlinhas() - 1; i >= 0; i--) {
            for (int j = nesimalinha(i).length() - 1; j >= 0; j--) {
                if (p1.linha == p2.linha) {
                    if ((i >= p1.linha && i <= p2.linha && j >= p1.coluna && j <= p2.coluna) || (i <= p1.linha && i >= p2.linha && j <= p1.coluna && j >= p2.coluna)) {
                        lineList.get(i).deleteCharAt(j);
                        movePrev();
                        if (lineList.get(i).length() == 0 && lineList.size() > 1)
                            lineList.remove(i);
                    }
                }

                else{
                    if (i >= p1.linha && i <= p2.linha){

                        if(i==p1.linha && j>=p1.coluna){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }
                        else if(i!=p1.linha && i!=p2.linha){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }

                        else if(i==p2.linha && j<=p2.coluna){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }

                    }

                    else if(i <= p1.linha && i >= p2.linha){
                        if(i==p2.linha && j>=p2.coluna){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }
                        else if(i!=p1.linha && i!=p2.linha){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }
                        else if(i==p1.linha && j<=p1.coluna){
                            lineList.get(i).deleteCharAt(j);
                            movePrev();
                            if (lineList.get(i).length() == 0 && lineList.size() > 1)
                                lineList.remove(i);
                        }
                    }
                }
            }
        }
    }

    /**
     * copia o que foi selecionado
     *
     * @param p1   posicao inicial
     * @param p2   posicao final
     */
    public void copy(Position p1, Position p2){
        linelist2.clear();
        StringBuilder clipBoard;

        for (int i=0; i<nlinhas(); i++){
            clipBoard = new StringBuilder();
            for (int j=0; j<nesimalinha(i).length(); j++){
                if(p1.linha==p2.linha){
                    if ((i >= p1.linha && i <= p2.linha && j >= p1.coluna && j <= p2.coluna) || (i <= p1.linha && i >= p2.linha && j <= p1.coluna && j >= p2.coluna))
                        clipBoard.append(nesimalinha(i).charAt(j));
                }
                else{
                    if (i >= p1.linha && i <= p2.linha){

                        if(i==p1.linha && j>=p1.coluna)
                            clipBoard.append(nesimalinha(i).charAt(j));

                        else if(i!=p1.linha && i!=p2.linha)
                            clipBoard.append(nesimalinha(i).charAt(j));

                        else if(i==p2.linha && j<=p2.coluna)
                            clipBoard.append(nesimalinha(i).charAt(j));

                    }

                    else if(i <= p1.linha && i >= p2.linha){
                        if(i==p2.linha && j>=p2.coluna)
                            clipBoard.append(nesimalinha(i).charAt(j));

                        else if(i!=p1.linha && i!=p2.linha)
                            clipBoard.append(nesimalinha(i).charAt(j));

                        else if(i==p1.linha && j<=p1.coluna)
                            clipBoard.append(nesimalinha(i).charAt(j));
                    }
                }
            }
            if (clipBoard.length()!=0)
                linelist2.add(clipBoard);
        }
    }

    /**
     * corta o que foi selecionado
     *
     * @param p1   posicao inicial
     * @param p2   posicao final
     */
    public void cut(Position p1, Position p2) {
        copy(p1, p2);
        removeMarcado(p1, p2);
    }

    /**
     * cola o que foi copiado/cortado anteriormente
     */
    public void paste() {
        String palavra[] = new String[linelist2.size()];

        for(int i=0; i<linelist2.size(); i++)
            palavra[i]=linelist2.get(i).toString();

        if (palavra.length > 0) {
            for(int i=0; i<palavra[0].length();i++)
                insert(palavra[0].charAt(i));
            for(int i = 1; i < palavra.length; i++) {
                insert('\n');
                for (int j = 0; j < palavra[i].length(); j++)
                    insert(palavra[i].charAt(j));
            }
        }
    }

    /**
     * Remove a ultima alteracao
     * @param ed        ultima operacao efetuada
     */
    public void undo(Edit ed){
        /*
        StringBuilder old = clipBoard;
        Position oldCursor = this.cursor;
        if (ed.op == Edit.EditOp.CUT || ed.op == Edit.EditOp.DELETE) {
            clipBoard = ed.getContent();
            cursor.setLinha(ed.first.getLinha());
            cursor.setColuna(ed.first.getColuna());
            paste();
            clipBoard = old;
            cursor = oldCursor;
            undoList.remove(0);
        }
        else if (ed.op == Edit.EditOp.INSERT || ed.op == Edit.EditOp.PASTE){
            cursor.setLinha(ed.first.getLinha());
            cursor.setColuna(ed.first.getColuna());
            setMark(ed.last.getLinha(), ed.last.getColuna()-1);
            removeMarcado(ed.first, ed.last);   //VERIFICAR se as posicoes mandadas sao as corretas
            clipBoard = old;
            cursor = ed.first;
            unsetMark();
        }*/
    }

    /**
     * Remove a ultima alteracao
     */
    public void undo(){
        if(undoList.size() != 0) {
            undo(undoList.get(0));
            undoList.remove(0);
        }
    }
}