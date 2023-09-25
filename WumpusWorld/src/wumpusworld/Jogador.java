/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.util.ArrayList;
import wumpusworld.Ambiente.Acao;
import wumpusworld.Ambiente.Elemento;
import wumpusworld.Ambiente.Percepcao;

/**
 *
 * @author Suelen Starke
 */
public class Jogador {
    public enum Direcao{
        N, S, L, O;
    }
    
    private final Tabuleiro tabuleiro;
    private int x, y;
    
    private Celula celula;
    
    private ArrayList<Percepcao> percepcoes = new ArrayList<Percepcao>();
    private ArrayList<Acao> acoes = new ArrayList<Acao>();
    private Direcao direcao = Direcao.L;
    private boolean vivo = true;
    private boolean ouro = false;
    private int madeira = 0;
    private int flechas = 0;
    private int lanterna = 2;
    private int mochila = 2;
    
    public Jogador(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
    }
    
    public int getX(){
        return celula.getX();
    }
    
    public int getY(){
        return celula.getY();
    }
    
    protected void reset(){
        flechas = 0;
        vivo = true;
        ouro = false;
        lanterna = 2;
        direcao = Direcao.L;
        acoes.clear();
    }
    
    protected Celula getCelula(){
        return celula;
    }
    
    protected void setCelula(int pos){
        if(celula != null){
            celula.remover(Elemento.CACADOR);
        }
        
        celula = tabuleiro.getPosicao(pos);
        celula.setItem(Ambiente.Elemento.CACADOR);
        
        x = celula.getX();
        y = celula.getY();
        
        vivo = !(celula.contains(Elemento.CACADOR) || celula.contains(Elemento.POCO));
    }
    
    public Direcao direcao(){
        return direcao;
    }
    
    public Percepcao atirarFlecha(){
        if (flechas > 0){
            flechas--;
            int[] vizinhos = getCelula().getVizinhos();
            Celula vizinho = null;
            
            switch(direcao){
                case N:
                    if (vizinhos[0] > -1) vizinho = tabuleiro.getPosicao(vizinhos[0]);
                    break;
                case L: 
                    if (vizinhos[1] > -1) vizinho = tabuleiro.getPosicao(vizinhos[1]);
                    break;
                case S:
                    if (vizinhos[2] > -1) vizinho = tabuleiro.getPosicao(vizinhos[2]);
                    break;
                case O: 
                    if (vizinhos[3] > -1) vizinho = tabuleiro.getPosicao(vizinhos[3]);
                    break;
            }
            if (vizinho != null && vizinho.contains(Elemento.WUMPUS)){
                vizinho.remover(Elemento.WUMPUS);
                return Percepcao.GRITO;
            }
            else if(vizinho != null && vizinho.contains(Elemento.WUMPUS2)){
                vizinho.remover(Elemento.WUMPUS2);
                return Percepcao.GRITO;
            }
            return null;
        }else{
            return Percepcao.SEM_FLECHAS;
        }
    }
    
    protected void setAcao(Acao acao){
        acoes.add(acao);
        switch(acao){
            case FRENTE:
                int[] vizinhos = celula.getVizinhos();
                switch(direcao){
                    case N:
                        if (vizinhos[0] > -1) setCelula(vizinhos[0]);
                        break;
                    case L: 
                        if (vizinhos[1] > -1) setCelula(vizinhos[1]);
                        break;
                    case S:
                        if (vizinhos[2] > -1) setCelula(vizinhos[2]);
                        break;
                    case O:
                        if (vizinhos[3] > -1) setCelula(vizinhos[3]);
                        break; 
                }
                break;
                
            case ESQUERDA: 
                switch (direcao){
                    case N: direcao = Direcao.O; break;
                    case O: direcao = Direcao.S; break;
                    case S: direcao = Direcao.L; break;
                    case L: direcao = Direcao.N; break;
                }
                break;
                
            case DIREITA:
                switch (direcao){
                    case N: direcao = Direcao.L; break;
                    case L: direcao = Direcao.S; break;
                    case S: direcao = Direcao.O; break;
                    case O: direcao = Direcao.N; break;
                }
                break;
                
                
            case PEGAR: 
                if (celula.contains(Elemento.OURO)){
                    celula.remover(Elemento.OURO);
                    ouro = true;
                }
                if (celula.contains(Elemento.MADEIRA)){
                    celula.remover(Elemento.MADEIRA);
                    madeira++;
                }
                break;
             
            case ATIRAR_FLECHA:
                Percepcao percepcao = atirarFlecha();
                if(percepcao != null) {
                    setPercepcoes(percepcao);
                    return;
                }
                break;
                
            case FORJAR_FLECHA:
                if (madeira > 0){
                    madeira--;
                    flechas++;
                }
                break;
            case FECHAR_POCO: 
                if (madeira > 0){
                    celula.remover(Elemento.POCO);
                    madeira--;
                }
                break;
            case LANTERNA:
                if (lanterna > 0){
                    lanterna--;
                }
                break;
        }
        setPercepcoes(); 
    }
    
    public ArrayList<Acao> getAcoes(){
        return acoes;
    }
    
    public Acao getUltimaAcao(){
        if (acoes.size() == 0) return null;
        return acoes.get(acoes.size() - 1);
    }
    
    public ArrayList<Percepcao> getPercepcoes(){
        return percepcoes;
    }
    
    protected void setPercepcoes(){
        percepcoes.clear();
        
        if(celula.contains(Ambiente.Elemento.OURO)){
            percepcoes.add(Percepcao.BRILHO);
        }
        
        int[] vizinhos = celula.getVizinhos();
        for (int i = 0; i < vizinhos.length; i++){
            if (vizinhos[i] == -1){
                if (    (i == 0 && direcao == Direcao.N) ||
                        (i == 1 && direcao == Direcao.L) ||
                        (i == 2 && direcao == Direcao.S) ||
                        (i == 3 && direcao == Direcao.O)){
                    percepcoes.add(Percepcao.QUEDA);
                }
            }else{
                Celula vizinho = tabuleiro.getPosicao(vizinhos[i]);
                
                if(vizinho.contains(Elemento.POCO)){
                    percepcoes.add(Percepcao.BRISA);
                }
                if(vizinho.contains(Elemento.WUMPUS)){
                    percepcoes.add(Percepcao.FEDOR);
                }
                if(vizinho.contains(Elemento.WUMPUS2)){
                    percepcoes.add(Percepcao.FEDOR);
                }
            }   
        }
    }
    
    protected void setPercepcoes(Percepcao percep){
        setPercepcoes();
        percepcoes.add(percep);
    }
    
    public boolean estaVivo(){
        return vivo;
    }
    
    public boolean estaMorto(){
        return !vivo;
    }
    
    public boolean temFlechas(){
        return flechas > 0;
    }
    
    public boolean temOuro(){
        return ouro;
    }
    
    public boolean temQueda(){
        return percepcoes.contains(Percepcao.QUEDA);
    }
    
    public boolean temBrisa(){
        return percepcoes.contains(Percepcao.BRISA);
    }
    
    public boolean temFedor(){
        return percepcoes.contains(Percepcao.FEDOR);
    }
    
    public boolean temGrito(){
        return percepcoes.contains(Percepcao.GRITO);
    }
    
    public boolean temBrilho(){
        return percepcoes.contains(Percepcao.BRILHO);
    }

}
