/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

import java.util.HashSet;
import wumpusworld.Ambiente.Elemento;
/**
 *
 * @author Suelen Starke
 */
public class Celula {
    private int x, y, h, w;
    private HashSet<Ambiente.Elemento> elementos = new HashSet<Elemento>();
    
    public Celula(int pos, int width, int heigth){
        x = pos % width;
        y = pos / width;
        w = width;
        h = heigth;
        clear();
    }
    
    public int getIndex(){
        return x + y * w;
    }
    
    public int getIndex(int x, int y){
        return x + y * w;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int[] getVizinhos(){
        int[] vizinhos = { -1, -1, -1, -1};
        
        int norte = y - 1;
        int sul = y + 1;
        int oeste = x - 1;
        int leste = x + 1;
        
        if(norte >= 0) vizinhos[0] = getIndex(x, norte);
        if(sul < h)    vizinhos[2] = getIndex(x, sul);
        if(leste < w)  vizinhos[1] = getIndex(leste, y);
        if(oeste >= 0) vizinhos[3] = getIndex(oeste, y);
        
        return vizinhos;
    }
    
    public void clear(){
        elementos.clear();
    }
    
    public void remover(Ambiente.Elemento item){
        elementos.remove(item);
    }
    
    public boolean estaVazio(){
        return elementos.isEmpty();
    }
    
    public boolean contains(Elemento elemento){
        return elementos.contains(elemento);
    }
    
    public void setItem(Elemento elemento){
        elementos.add(elemento);
    }

}
