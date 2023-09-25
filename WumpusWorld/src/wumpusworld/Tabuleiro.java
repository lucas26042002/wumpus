/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import wumpusworld.Ambiente.Acao;
import wumpusworld.Ambiente.Elemento;
import wumpusworld.Ambiente.Percepcao;

/**
 *
 * @author Suelen Starke
 */
public class Tabuleiro {
    private static final int QTD_OURO = 1;
    private static final int QTD_WUMPUS = 1;
    private static final int QTD_POCOS = 40;
    private static final int QTD_MADEIRA = 20;
    
    private final int largura;
    private final int altura;
    private final int posInicial;
    
    private int ouro = QTD_OURO;
    private int pocos = QTD_POCOS;
    private int madeiras = QTD_MADEIRA;
    private int wumpus = QTD_WUMPUS;
    private int wumpus2 = QTD_WUMPUS;
    
    private boolean aleatorio = true;
    private HashMap<Integer, Ambiente.Elemento> itens = new HashMap<Integer, Elemento>();
    
    private String agenteName;
    private final Jogador jogador;
    private final Celula[] celulas;
    
    public Tabuleiro (int l, int a) {
        this.largura = l;
        this.altura = a;
        
        celulas = new Celula[largura * altura];
        for(int i = 0; i < altura * largura; i++){
            celulas[i] = new Celula(i, largura, altura);
        }
        
        posInicial = getIndex(14, 0);
        
        jogador = new Jogador(this);
    }
    
    public void execute(Agente agente){
        agenteName = agente.getClass().getName();
        
        for (Jogador jogador : run()) {
            agente.antesAcao(jogador);
            Acao acoes = agente.getAcao(jogador);
            jogador.setAcao(acoes);
            agente.depoisAcao(jogador);
        }
    }
    
    private Jogo run(){
        reset();
        return new Jogo(this);
    }
    
    public String getAgenteName(){
        return agenteName;
    }
    
    public void setPocos(int value){
        pocos = value;
    }
    
    public void setPoco(int x, int y){
        setItem(Elemento.POCO, x, y);
    }
    
    public void setWumpus(int value){
        wumpus = value;
    }
    
    public void setWumpus(int x , int y){
        setItem(Elemento.WUMPUS, x, y);
    }
    
    public void setGold(int x, int y){
        setItem(Elemento.OURO, x, y);
    }
    
    
    private void setItem(Elemento elemento, int x, int y){
        Celula celula = getPosicao(x, y);
        if(celula.estaVazio()){
            celula.setItem(elemento);
        }
        
        itens.put(celula.getIndex(), elemento);
        aleatorio = false;
    }
    
    private void setRandom(Ambiente.Elemento elemento, int qtd ){
        Random random = new Random();
        int tentativas = 0;
        int[] posSegura = jogador.getCelula().getVizinhos();
        
        for(int i = 0; i < qtd; i++){
            Celula pos;
            while(true){
                int z = random.nextInt(altura * largura - 1);
                pos = celulas[z];
                if(pos.estaVazio() && 
                        z != posSegura[0] &&
                        z != posSegura[1] &&
                        z != posSegura[2] &&
                        z != posSegura[3]){
                    pos.setItem(elemento);
                    break;
                }
                if(tentativas >= 20){
                    System.out.println("Incrementa essa porra ai caralho");
                } 
                else{
                    tentativas++;
                }
            }
        }
    }
    
    public int getIndex(int x, int y){
        return (x + y * largura);
    }
    
    public Celula getPosicao(int index){
        return celulas[index];
    }
    
    public Celula getPosicao(int x, int y){
        int i = getIndex(x, y);
        return celulas[i];
    }
    
    public Jogador getJogador(){
        return jogador;
    }
    
    public int getLargura(){
        return largura;
    }
    
    public int getAltura(){
        return altura;
    }
    
    public Ambiente.Resultado getResultado(){
        if (jogador.estaVivo() && jogador.temOuro() && jogador.getCelula().getIndex() == posInicial){
            return Ambiente.Resultado.VITORIA;
        }
        return Ambiente.Resultado.DERROTA;
    }
            
    public void reset(){
        for(int i = 0; i < celulas.length; i++){
            celulas[i].clear();
        }
        
        jogador.setCelula(posInicial);
        jogador.reset();
        if(aleatorio){
            setRandom(Elemento.WUMPUS, 1);
            setRandom(Elemento.WUMPUS2, 1);
            setRandom(Elemento.OURO, 1);  
            setRandom(Elemento.POCO, 5); 
            setRandom(Elemento.MADEIRA, 2); 
        }else{
            for(int index : itens.keySet()){
                Celula celula = getPosicao(index);
                celula.setItem(itens.get(index));
            }
        }
    }   
}
