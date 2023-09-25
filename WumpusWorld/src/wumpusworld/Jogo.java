/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.util.NoSuchElementException;
import java.util.Iterator;

import wumpusworld.Ambiente.Acao;
import wumpusworld.Ambiente.Resultado;

/**
 *
 * @author Suelen Starke
 */
public class Jogo implements Iterable<Jogador>, Iterator<Jogador>{
    private final Tabuleiro tabuleiro;
    private int iterations = 0;
    private int maxIterations;
    
    public Jogo(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;
        this.maxIterations = 10000;
    }
    
    public Iterator<Jogador> iterator(){
        return this;
    }
    
    public boolean hasNext(){
        Jogador jogador = tabuleiro.getJogador();
        return iterations < 1000 && tabuleiro.getResultado() != Resultado.VITORIA &&
                jogador.estaVivo() && jogador.getUltimaAcao() != Acao.SAIR;
        
    }
    
    public Jogador next(){
        if (!hasNext());
        iterations++;
        return tabuleiro.getJogador();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
   
}
