/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 1.0
 */
public class Edit {
    /**
     * Enumeracao das operacoes de modificacao
     */
    enum EditOp {INSERT, DELETE, PASTE, CUT}

    EditOp op;
    Position first, last;
    private StringBuilder str;
    // outros argumentos necessários

    /**
     * Metodo contrutor da classe responsavel pela edicao da ultima alteracao
     * @param op        operacao realizada
     * @param first     posicao inicial
     * @param last      posicao final
     */
    Edit(EditOp op, Position first, Position last) {
        this.op = op;
        this.first = first;
        this.last = last;
    }


    /**
     * Altera o texto editado
     * @param str   fragmento de texto
     */
    public void setContent(StringBuilder str) {
        this.str = str;
    }

    /**
     * Retorna o texto editado
     * @return      fragmento de texto
     */
    public StringBuilder getContent() {
        return new StringBuilder(str);
    }
}