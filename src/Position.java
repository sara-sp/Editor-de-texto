/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.0
 */
public class Position {
    int linha, coluna;

    /**
     * Metodo contrutor da classe que determina a posicao do cursor
     *
     * @param linha        indica a linha atual
     * @param coluna       indica a coluna atual
     */
    Position (int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    /**
     * determina a linha atual
     *
     * @return              retorna o numero da linha atual
     */
    int getLinha(){
        return linha;
    }

    /**
     * altera a linha atual
     *
     * @param linha         modifica o valor da linha para a desejada
     */
    void setLinha(int linha){
        this.linha = linha;
    }

    /**
     * determina a coluna atual
     *
     * @return              retorna o numero da coluna atual
     */
    int getColuna(){
        return coluna; 
    }

    /**
     * altera a coluna atual
     *
     * @param coluna         modifica o valor da coluna para a desejada
     */
    void setColuna(int coluna){
        this.coluna = coluna;
    }


    /**
     * par de Strings
     *
     * @return         retorna uma Strings contendo o par (linha, coluna)
     */
    public String toString(){
        return ("("+getLinha() + "," + getColuna()+")");
    }
}