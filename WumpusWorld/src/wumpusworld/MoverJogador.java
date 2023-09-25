/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import wumpusworld.WumpusWorld;
import wumpusworld.Ambiente.Acao;

/**
 *
 * @author samue
 */
public class MoverJogador implements Agente{
    private int frame;
    
    public MoverJogador(int frame){
     this.frame = frame;
    }
    
    
    public Acao getAcao(Jogador jogador, ActionEvent e){
        int x = jogador.getX();
        int y = jogador.getY();
        
        switch (frame) {
            case 1:
                jogador.setAcao(Acao.FRENTE);
                break;
            case 2:
                jogador.setAcao(Acao.DIREITA);
                break;
            case 3:
                jogador.setAcao(Acao.ESQUERDA);
                break;
            default:
                break;
        }
           
        
        
        
        return null;
        
    }
    
    
    
    
    
    
    
    
    
    
    @Override
    public Ambiente.Acao getAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void depoisAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void antesAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
