import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author            Andre Pereira up201407074@fc.up.pt
 * @author            Sara Pereira  up201304112@fc.up.pt
 * @version 2.0
 */
public class BufferView {
    private Buffer b;
    private int width = 150, height=40;
    private Terminal term;
    private int x, y;
    private Position p1, p2;
    private int auxOpen, auxGuardaComo, auxInicio, auxMarca, auxCopia;
    private boolean marca;
    private FileBuffer fb;
    private String ficheiro, aux;
    private int maxColuna,maxLinha;

    /**
     * Metodo construtor da classe responsavel pela visao grafica do editor de texto
     * @param b     variavel que representa a classe que contem os metodos da visao logica
     * @throws IOException
     */
    BufferView(Buffer b) throws java.io.IOException, InterruptedException {
        this.b = b;
        this.term = TerminalFacade.createSwingTerminal(width, height);
        this.term.enterPrivateMode();
        ficheiro = aux = "";
        x = y = 1;
        p1 = p2 = new Position(1, 1);
        marca = false;
        fb = new FileBuffer();
        auxInicio = auxOpen = auxGuardaComo = auxMarca = auxCopia = 0;
        term.setCursorVisible(false);
        desenhaBorda(term, 0);
        maxColuna=1;maxLinha=1;
    }

    /**
     * Recebe os inputs do utilizador
     * @throws IOException
     */
    private void input() throws IOException, InterruptedException {
        while(true){
            Thread.sleep(16,66);
            Key k = term.readInput();
            if(k!=null) {
                switch (k.getKind()) {
                    //cursor para cima
                    case ArrowUp:
                        b.movePrevLine();
                        atualizaCursor(term);
                        atualizaFicheiro(term, true);
                        break;
                    //cursor baixo
                    case ArrowDown:
                        b.moveNextLine();
                        atualizaCursor(term);
                        atualizaFicheiro(term, true);
                        break;
                    //cursor para a esquerda
                    case ArrowLeft:
                        b.movePrev();
                        atualizaCursor(term);
                        atualizaFicheiro(term, true);
                        break;
                    //cursor para a direita
                    case ArrowRight:
                        b.moveNext();
                        atualizaCursor(term);
                        atualizaFicheiro(term,true);
                        break;
                    //apaga caracter
                    case Backspace:
                        if(auxOpen==1 || auxGuardaComo==1){
                            x--;
                            term.moveCursor(x,y);
                            term.putCharacter(' ');
                            term.moveCursor(x,y);

                            ficheiro = ficheiro.trim();                                 //retira caracter ' '
                            ficheiro = ficheiro.substring(0, ficheiro.length()-1);      //apaga ultima letra da string
                        }

                        if(auxInicio==1 && auxOpen==0 && auxGuardaComo==0){
                            b.deleteAt(b.cursor);
                            imprimeTexto(term);
                            term.putCharacter(' ');
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }

                        break;
                    //move cursor para o final do documento
                    case End:
                        if(auxInicio==1){
                            b.cursor.setColuna(b.nesimalinha(b.nlinhas()-1).length());
                            b.cursor.setLinha(b.lineList.size()-1);
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }
                        break;
                    case Enter:
                        //Abre janela do editor
                        if(auxInicio==0 && auxOpen==0){
                            auxInicio = 1;
                            auxOpen = 0;
                            term.clearScreen();
                            ficheiro="NovoFicheiro.txt";
                            fb.savePath = Paths.get(ficheiro); //Cria novo ficheiro
                            desenhaBorda(term, 0);
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }

                        //Escreve uma mudanca de linha
                        if(auxInicio==1 && auxGuardaComo==0 && auxOpen==0){
                            b.insertAt(b.cursor, '\n');
                            imprimeTexto(term);
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }

                        //Abre o ficheiro durante a primeira janela
                        if(auxOpen==1 && auxInicio==0){
                            try{
                                auxOpen=0;
                                auxInicio=1;
                                fb.open(Paths.get(ficheiro));
                                term.clearScreen();
                                desenhaBorda(term, 1);
                            }
                            catch (IOException e){
                                auxOpen=1;
                                auxInicio=2;

                                x=57;
                                y=36;
                                term.moveCursor(x,y);

                                for(int i=0; i<ficheiro.length();i++){
                                    term.putCharacter(' ');
                                    x++;
                                    term.moveCursor(x,y);
                                }

                                ficheiro="";
                                term.applyForegroundColor(Terminal.Color.RED);
                                desenhaM(63, 38, "Ficheiro nao encontrado", term);
                                term.moveCursor(57, 36);

                            }
                        }

                        //Abre o ficheiro durante o editor de texto
                        if(auxOpen==1 && auxInicio==1){
                            try{
                                auxOpen=0;
                                auxInicio=1;
                                fb.open(Paths.get(ficheiro));
                                term.clearScreen();
                                desenhaBorda(term, 1);
                            }
                            catch (IOException e){
                                auxOpen=1;
                                auxInicio=2;

                                x=45+48+18;
                                y=36;
                                term.moveCursor(45+48+18,36);

                                for(int i=0; i<ficheiro.length();i++){
                                    term.putCharacter(' ');
                                    x++;
                                    term.moveCursor(x,y);
                                }
                                ficheiro="";
                                term.applyForegroundColor(Terminal.Color.RED);
                                term.applySGR(Terminal.SGR.ENTER_BOLD);
                                desenhaM(31+33, 36, "Ficheiro nao encontrado", term);
                                term.applySGR(Terminal.SGR.EXIT_BOLD);
                                term.moveCursor(45+48+18,36);

                            }
                        }

                        //Salva o arquivo
                        if(auxGuardaComo==1){
                            auxGuardaComo=0;
                            for(int i=0; i<"Nome do ficheiro: ".length()+ficheiro.length(); i++) {
                                desenhaM(45 + 48 + i, 36, " ", term);
                                desenhaM(16+18+i, 36, " ", term);
                            }
                            fb.aux = new StringBuilder("");
                            for(int i=0; i<b.lineList.size(); i++){
                                fb.aux.append(b.lineList.get(i));
                                fb.aux.append('\n');
                            }
                            fb.saveAs(Paths.get(ficheiro));
                            atualizaFicheiro(term, false);
                        }

                        break;
                    //sai do terminal
                    case Escape:
                        term.exitPrivateMode();
                        System.exit(0);
                    //move cursor para o inicio do documento
                    case Home:
                        if(auxInicio==1){
                            b.cursor.setColuna(0);
                            b.cursor.setLinha(0);
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }
                        break;
                    //da espaçamento na linha atual
                    case Tab:
                        if(auxInicio==1){
                            for(int i=0; i<4; i++){
                                b.insertAt(b.cursor,' ');
                                imprimeTexto(term);
                            }
                            atualizaCursor(term);
                            atualizaFicheiro(term, true);
                        }

                        break;
                    case NormalKey:
                        if(k.isCtrlPressed()){
                            //Inserir Marca - CTRL + A
                            if ((k.getCharacter()=='a' || k.getCharacter()=='A') && auxInicio==1){
                                auxMarca++;
                                term.applySGR(Terminal.SGR.ENTER_BOLD);

                                if(auxMarca==1){
                                    p1 = new Position(vistaFisica(b.cursor).getLinha()-1,vistaFisica(b.cursor).getColuna()-1);

                                    for(int i=0; i<"<CTRL+A> Iniciar marca".length(); i++)
                                        desenhaM(16+18+i, 38, " ", term);

                                    for(int i=0; i<"<CTRL+V> Cortar".length(); i++){
                                        desenhaM(31+33+i, 38, " ", term);
                                        desenhaM(45+48+i, 38, " ", term);
                                    }

                                    desenhaM(16+18, 38, "<CTRL+A> Parar marca", term);
                                }
                                else if(auxMarca==2){
                                    auxMarca=0;
                                    p2 = new Position(vistaFisica(b.cursor).getLinha()-1,vistaFisica(b.cursor).getColuna()-1);
                                    marca = true;

                                    for(int i=0; i<"<CTRL+A> Parar marca".length(); i++)
                                        desenhaM(16+18+i, 38, " ", term);

                                    desenhaM(16+18, 38, "<CTRL+A> Iniciar marca", term);
                                    desenhaM(31+33, 38, "<CTRL+C> Copiar", term);
                                    desenhaM(45+48, 38, "<CTRL+X> Cortar", term);
                                }

                                term.applySGR(Terminal.SGR.EXIT_BOLD);
                                atualizaFicheiro(term, true);
                            }
                            //Copiar texto Selecionado - CTRL + C
                            else if ((k.getCharacter()=='c' || k.getCharacter()=='C') && auxInicio==1 && marca && auxMarca!=1){
                                for(int i=0; i<"<CTRL+V> Cortar".length(); i++){
                                    desenhaM(31+33+i, 38, " ", term);
                                    desenhaM(45+48+i, 38, " ", term);
                                }
                                b.copy(p1, p2);
                                auxCopia=1;

                                term.applySGR(Terminal.SGR.ENTER_BOLD);
                                desenhaM(31+33, 38, "<CTRL+V> Colar", term);
                                term.applySGR(Terminal.SGR.EXIT_BOLD);

                                marca=false;
                                atualizaFicheiro(term, true);
                            }

                            //Guardar Ficheiro como - CTRL + G
                            else if ((k.getCharacter()=='g' || k.getCharacter()=='G') && auxInicio==1){
                                auxGuardaComo=1;
                                ficheiro="";
                                term.applySGR(Terminal.SGR.ENTER_BOLD);
                                term.applyForegroundColor(Terminal.Color.GREEN);
                                desenhaM(45+48,36, "Nome do ficheiro: ", term);
                                x=45+48+18;
                                y=36;
                                term.moveCursor(45+48+18,36);
                                term.setCursorVisible(true);
                                term.applyForegroundColor(255,255,255);
                                term.applySGR(Terminal.SGR.EXIT_BOLD);
                            }
                            //Abrir nova janela do Lanterna - CTRL + N
                            else if ((k.getCharacter()=='n' || k.getCharacter()=='N')){
                                new Thread("NovaJanela"){
                                    public void run(){
                                        Buffer nBuffer = new Buffer();
                                        try{
                                            try {
                                                new BufferView(nBuffer);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                            //Abrir Ficheiro - CTRL + O
                            else if ((k.getCharacter()=='o' || k.getCharacter()=='O')){
                                auxOpen=1;
                                ficheiro="";
                                if(auxInicio==0){
                                    for(int i=0; i<"Precione <ENTER> para editar um novo ficheiro".length();i++)
                                        desenhaM(53+i, 31," ", term);

                                    term.applySGR(Terminal.SGR.ENTER_BOLD);
                                    desenhaM(60, 31,"Precione <ENTER> para confirmar", term);
                                    term.applyForegroundColor(Terminal.Color.RED);
                                    desenhaM(61, 34, "Indique o nome do ficheiro:", term);

                                    // ┃    ━   ┏    ┓  ┗ ┛
                                    desenhaM(56, 35,"┏", term);
                                    desenhaM(56, 36,"┃", term);
                                    desenhaM(56, 37,"┗", term);
                                    desenhaM(92, 35,"┓", term);
                                    desenhaM(92, 36,"┃", term);
                                    desenhaM(92, 37,"┛", term);

                                    for(int i=0; i<35; i++){
                                        desenhaM(57+i, 35,"━", term);
                                        desenhaM(57+i, 37,"━", term);
                                    }

                                    x=57;
                                    y=36;
                                    term.moveCursor(x,y);
                                    term.setCursorVisible(true);
                                }
                                else{
                                    term.applySGR(Terminal.SGR.ENTER_BOLD);
                                    term.applyForegroundColor(Terminal.Color.GREEN);
                                    desenhaM(45+48,36, "Nome do ficheiro: ", term);
                                    x=45+48+18;
                                    y=36;
                                    term.moveCursor(45+48+18,36);
                                    term.setCursorVisible(true);
                                }
                                term.applySGR(Terminal.SGR.EXIT_BOLD);
                            }
                            //Guardar Ficheiro - CTRL + S
                            else if ((k.getCharacter()=='s' || k.getCharacter()=='S') && auxInicio==1){
                                fb.aux = new StringBuilder("");
                                for(int i=0; i<b.lineList.size(); i++){
                                    fb.aux.append(b.lineList.get(i));
                                    fb.aux.append('\n');
                                }

                                fb.save();

                                x=16+18;
                                y=36;
                                term.moveCursor(x,y);

                                for(int i=0; i<ficheiro.length()+1; i++)
                                    term.putCharacter(' ');

                                term.applyForegroundColor(Terminal.Color.GREEN);
                                term.applySGR(Terminal.SGR.ENTER_BOLD);
                                desenhaM(16+18,36, ficheiro, term);
                                term.applyForegroundColor(255,255,255);
                                term.applySGR(Terminal.SGR.EXIT_BOLD);
                                atualizaCursor(term);
                                atualizaFicheiro(term, false);
                            }
                            //Colar texto Selecionado - CTRL + V                                                                            ####ERRO AO COLAR
                            else if ((k.getCharacter()=='v' || k.getCharacter()=='V') && auxInicio==1 && (auxCopia==1 || auxCopia==2)){
                                for(int i=0; i<"<CTRL+V> Colar".length(); i++)
                                    desenhaM(31+33+i, 38, " ", term);

                                b.paste();
                                auxCopia=0;
                                imprimeTexto(term);
                                atualizaFicheiro(term, true);
                            }
                            //Cortar texto Selecionado - CTRL + X
                            else if ((k.getCharacter()=='x' || k.getCharacter()=='X') && auxInicio==1 && marca && auxMarca!=1){

                                for(int i=0; i<"<CTRL+V> Cortar".length(); i++){
                                    desenhaM(31+33+i, 38, " ", term);
                                    desenhaM(45+48+i, 38, " ", term);
                                }

                                for(int i=0; i<width-2; i++){
                                    for(int j=0; j<34; j++){
                                        desenhaM(i+1, j+1, " ", term);
                                    }
                                }
                                b.cut(p1, p2);
                                auxCopia=2;
                                imprimeTexto(term);

                                term.applySGR(Terminal.SGR.ENTER_BOLD);
                                desenhaM(31+33, 38, "<CTRL+V> Colar", term);
                                term.applySGR(Terminal.SGR.EXIT_BOLD);

                                marca=false;
                                atualizaFicheiro(term, true);
                            }
                        }
                        //Inserir caracteres
                        else {
                            term.applyForegroundColor(255,255,255);
                            //Escreve nome do ficheiro
                            if(auxOpen==1 || auxGuardaComo==1){
                                term.putCharacter(k.getCharacter());
                                x++;
                                ficheiro += k.getCharacter();
                            }
                            //Escreve texto
                            if(auxInicio==1 && auxOpen==0 && auxGuardaComo==0){
                                b.insertAt(b.cursor, k.getCharacter());
                                atualizaFicheiro(term, true);
                                imprimeTexto(term);
                            }
                        }
                        break;

                }
            }
        }
    }

    /**
     * Metodo que vai atualizar o texto conforme o ficheiro aberto
     * @param term      util para utilizar os metodos da classe Terminal
     * @throws IOException
     */
    private void imprimeOpen(Terminal term) throws IOException{                                         //##### imprime 2x se aberto 2x
        term.moveCursor(1,1);
        b.lineList = fb.lineList;
        imprimeTexto(term);
        b.cursor.setColuna(fb.cursor.getColuna());
        b.cursor.setLinha(fb.cursor.getLinha());
        atualizaCursor(term);
        atualizaFicheiro(term, false);
    }

    /**
     * Metodo responsavel por imprimir texto proveniente do utlizador
     * @param term              util para utilizar os metodos da classe Terminal
     * @throws IOException
     */
    private void imprimeTexto(Terminal term) throws IOException{//###SE TEXTO ULTRAPASSAR ALTURA ATUALIZAR COMO DEVE SER
        atualizaFicheiro(term, true);
        maxColuna = Math.max(maxColuna, vistaFisica(b.cursor).getColuna());
        maxLinha = Math.max(maxLinha, vistaFisica(b.cursor).getLinha());
        if(vistaFisica(b.cursor).getLinha()>34)
            maxLinha = 34;

        for(int i=1; i<maxColuna;i++)
            for(int j=1; j<maxLinha;j++)
                desenhaM(i, j, " ", term);
        

        sai: 
            for(int i=0;i<arrayLinhas().length; i++){
                if(arrayLinhas().length<=34)
                    desenhaM(1, i+1, arrayLinhas()[i], term);
                else{
                    if(i==0){
                        for(int j=1; j<width-1;j++)
                            for(int k=1; k<35;k++)
                                desenhaM(j, k, " ", term);
                    }
    
                    for(int j=arrayLinhas().length-34; j<arrayLinhas().length; j++) {
                        System.out.println((j-(arrayLinhas().length-34))+1);
                        desenhaM(1, (j-(arrayLinhas().length-34))+1, arrayLinhas()[j], term);
    
                    }
                    break sai;
                }
            }

        atualizaCursor(term);
    }

    /**
     * Metodo reponsavel por converte as linhas fisicas para um array de strings
     * @return      retorna um array de strings
     */
    private String[] arrayLinhas(){
        String[]linhas = new String[vistaFisica(new Position(b.lineList.size(), b.nesimalinha(b.lineList.size()-1).length())).getLinha()-1];

        for(int i=0; i<linhas.length; i++) {
            linhas[i]="";
            for (int j=0; j<b.lineList.size(); j++) {
                for(int k=0; k<b.lineList.get(j).length();k++) {
                    if(i==vistaFisica(new Position(j,k)).getLinha()-1) {
                        linhas[i] += b.lineList.get(j).charAt(k);
                    }
                }
            }
        }

        return linhas;
    }

    /**
     * Metodo que converte uma posicao logica numa fisica
     * @param logica            posicao logica para converter
     * @return                  retorna a posicao fisica
     */
    private Position vistaFisica(Position logica){
        int lin = logica.getLinha();
        int col = logica.getColuna();

        int quociente = col/(width-2);
        int resto = col%(width-2);

        if(resto>=0)
            quociente++;

        if(resto<width)
            return new Position(quociente+lin, resto+1);
        else
            return null;
    }

    /*#########################
    #  Metodos para desenhar  #
    ###########################
    */

    /**
     * Metodo que vai imprimir a posicao do cursor
     * @param term      util para utilizar os metodos da classe Terminal
     */
    private void atualizaCursor(Terminal term){
        term.applySGR(Terminal.SGR.ENTER_BOLD);
        term.applyForegroundColor(Terminal.Color.GREEN);

        //Apaga possiveis letras a mais
        for(int i=0; i<aux.length(); i++)
            desenhaM(1 + 3 + i, 36, " ", term);

        desenhaM(1 + 3, 36, "cursor(" + vistaFisica(b.cursor).getColuna() + ", " + vistaFisica(b.cursor).getLinha() + ")", term);

        aux = "cursor(" + vistaFisica(b.cursor).getColuna() + ", " + vistaFisica(b.cursor).getLinha() + ")";

        if(b.cursor.getLinha()==0 && b.cursor.getColuna()==0)
            term.moveCursor(1, 1);

        if(arrayLinhas().length<34)
            term.moveCursor(vistaFisica(b.cursor).getColuna(), vistaFisica(b.cursor).getLinha());
        else 
            term.moveCursor(vistaFisica(b.cursor).getColuna(), 34);

        term.applySGR(Terminal.SGR.EXIT_BOLD);
        term.applyForegroundColor(255,255,255);
    }

    /**
     * Metodo que vai atualizando se o ficheiro esta guardado ou nao, imprimindo o nome dele
     * @param term          util para utilizar os metodos da classe Terminal
     * @param modificado    informa-nos se o ficheiro foi modificado depois do ultimo save
     */
    private void atualizaFicheiro(Terminal term, boolean modificado){
        term.applySGR(Terminal.SGR.ENTER_BOLD);
        term.applyForegroundColor(Terminal.Color.GREEN);

        //Apaga possiveis letras a mais
        for(int i=0; i<ficheiro.length(); i++)
            desenhaM(16+18+i, 36, " ", term);

        if(modificado)
            desenhaM(16+18, 36, ficheiro+"*" , term);

        else
            desenhaM(16+18, 36, ficheiro, term);


        term.applySGR(Terminal.SGR.EXIT_BOLD);
        term.applyForegroundColor(Terminal.Color.WHITE);
        atualizaCursor(term);
    }

    /**
     * Desenha grelha de jogo
     * @param term          util para utilizar os metodos da classe Terminal
     * @throws IOException
     */
    private void desenhaBorda(Terminal term, int auxOpen2) throws IOException, InterruptedException {
        //╔ ╗ ╝ ═ ╚ ║ ╣ ╠ ╬
        int separador = 35;

        term.applySGR(Terminal.SGR.ENTER_BOLD);
        term.applyForegroundColor(255,255,255);
        desenhaM(0,0, "╔", term);
        desenhaM(width,0, "╗", term);
        desenhaM(0,height, "╚", term);
        desenhaM(width,height, "╝", term);

        for(int i=1; i<height-1; i++){
            if(auxInicio==0){
                desenhaM(0, i, "║", term);
                desenhaM(width, i, "║", term);
            }
            else{
                if(i!=separador){
                    desenhaM(0, i, "║", term);
                    desenhaM(width, i, "║", term);
                }
            }
        }

        for(int i=1; i<width-1; i++){
            desenhaM(i,0, "═", term);
            desenhaM(i,height, "═", term);
            if(auxInicio!=0)
                desenhaM(i,separador, "═", term);
        }

        if(auxInicio==1){
            desenhaM(0, separador, "╠", term);
            desenhaM(width, separador, "╣", term);
            term.applyForegroundColor(Terminal.Color.GREEN);
            term.applyForegroundColor(255,255,255);

            desenhaM(1+3,separador+2, "<CTRL+O> Abrir arquivo", term);
            desenhaM(31+33,separador+2, "<CTRL+S> Salvar arquivo", term);
            desenhaM(16+18,separador+2, "<CTRL+G> Salvar arquivo como", term);
            desenhaM(45+48,separador+2, "<HOME> Ir para inicio", term);
            desenhaM(66+63,separador+2, "<END> Ir para fim", term);
            desenhaM(1+3,separador+3, "⇦ ⇧ ⇨ ⇩ Mover cursor", term);
            desenhaM(16+18,separador+3, "<CTRL+A> Iniciar marca", term);

            atualizaCursor(term);
            term.setCursorVisible(true);
        }
        else
            desenhaInicio(term);

        term.applySGR(Terminal.SGR.EXIT_BOLD);

        if(auxOpen2==1){
            atualizaCursor(term);
            imprimeOpen(term);
        }

        input();
    }

    /**
     * Desenha titulo inicial
     * @param term          util para utilizar os metodos da classe Terminal
     */
    private void desenhaInicio(Terminal term){
        term.applySGR(Terminal.SGR.ENTER_BOLD);
        term.applyForegroundColor(0, 204, 204);
        desenhaM(40,3, " .--.    .-. _  .-.                .-.         .-.             .-.      ", term);
        desenhaM(40,4, ": .--'   : ::_;.' `.               : :        .' `.           .' `.     ", term);
        desenhaM(40,5, ": `;   .-' :.-.`. .'.--. .--.    .-' : .--.   `. .'.--. .-.,-.`. .'.--. ", term);
        desenhaM(40,6, ": :__ ' .; :: : : :' .; :: ..'  ' .; :' '_.'   : :' '_.'`.  .' : :' .; :", term);
        desenhaM(40,7, "`.__.'`.__.':_; :_;`.__.':_;    `.__.'`.__.'   :_;`.__.':_,._; :_;`.__.'", term);

        term.applyForegroundColor(0, 128, 255);
        desenhaM(58,13, "      _.--._  _.--._", term);
        desenhaM(58,14, ",-=.-\"      \\       \"-._", term);
        desenhaM(58,15, "\\\\\\          \\          \\", term);
        desenhaM(58,16, " \\\\\\          \\          \\", term);
        desenhaM(58,17, "  \\\\\\          \\          \\", term);
        desenhaM(58,18, "   \\\\\\          \\          \\", term);
        desenhaM(58,19, "    \\\\\\          \\          \\", term);
        desenhaM(58,20, "     \\\\\\          \\          \\", term);
        desenhaM(58,21, "      \\\\\\     _:--:\\:_:--:_   \\", term);
        desenhaM(58,22, "       \\\\\\_.-\"      :      \"-._\\", term);
        desenhaM(58,23, "        \\`_..--\"\"--.;.--\"\"--.._=", term);
        desenhaM(58,24, "                     \"", term);

        term.applyForegroundColor(0, 204, 204);
        desenhaM(55, 30, "Precione <Ctrl+O> para abrir um ficheiro ", term);
        desenhaM(53, 31, "Precione <ENTER> para editar um novo ficheiro ", term);

    }

    /**
     * Metodo auxiliar que desenha qualquer string
     * @param x         posicao X do cursor
     * @param y         posicao Y do cursor
     * @param logo      string a desenhar
     * @param term      util para utilizar os metodos da classe Terminal
     */
    private void desenhaM(int x, int y, String logo, Terminal term){
        term.moveCursor(x, y);
        for(int i=0; i<logo.length(); i++)
            term.putCharacter(logo.charAt(i));
    }
}