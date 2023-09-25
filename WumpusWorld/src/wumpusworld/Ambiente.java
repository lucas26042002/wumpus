/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

/**
 *
 * @author Suelen Starke
 */
public class Ambiente {
    
    public enum Elemento{
        WUMPUS, WUMPUS2, POCO, MADEIRA, CACADOR, OURO;
    }
    
    public enum Percepcao{
        BRILHO, BRISA, FEDOR, GRITO, SEM_FLECHAS, QUEDA;
    }
    
    public enum Acao{
        FRENTE, ESQUERDA, DIREITA, PEGAR ,ATIRAR_FLECHA, FORJAR_FLECHA, FECHAR_POCO, LANTERNA, NADA, SAIR;
    }
    
    public enum Resultado{
        VITORIA, DERROTA;
    }
    
    protected static String getIcon (Elemento elemento){
        switch (elemento){
            case WUMPUS: return "W";
            case WUMPUS2: return "WW";
            case POCO: return "B";
            case MADEIRA: return "M";
            case CACADOR: return "C";
            case OURO: return "O";
        }
        return " ";
    }
    
    protected static String getIcon (Percepcao percepcao){
        switch (percepcao){
            case BRILHO: return "g";
            case BRISA: return "b";
            case FEDOR: return "f";
        }
        return " ";
    }
    
    protected static String getIcon (Jogador jogador){
        if(jogador.estaMorto()) return "d";
        
        switch(jogador.direcao()){
            case N: return "n";
            case L: return "l";
            case S: return "s";
            case O: return "o";
        }
        
        return "H";
    }
    
    
}
