/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wumpusworld;

import java.awt. *;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing. *;
import wumpusworld.Ambiente.Elemento;

import wumpusworld.Agente;
import wumpusworld.Ambiente;
import wumpusworld.Ambiente.Acao;
import wumpusworld.Jogador;
import wumpusworld.Jogador.Direcao;
import static wumpusworld.Jogador.Direcao.L;
import static wumpusworld.Jogador.Direcao.N;
import static wumpusworld.Jogador.Direcao.O;
import static wumpusworld.Jogador.Direcao.S;
/**
 *
 * @author Suelen Starke
 */
public class WumpusWorld extends JFrame implements ActionListener{
    private JPanel[][] janela;
    private final int tam = 15;
    
    Tabuleiro tab = new Tabuleiro(tam, tam);
    
    JPanel pTabuleiro = new JPanel();
    JPanel pOpcoes = new JPanel();
    JPanel pGame = new JPanel();
    
    JButton playB = new JButton("Jogar");
    JButton debugB = new JButton("Debug");
    JButton playableB = new JButton("Jogabilidade");
    JButton exitB = new JButton("Sair");
    
    JButton Frente = new JButton("Frente");
    JButton Direita = new JButton("Direita");
    JButton Esquerda = new JButton("Esquerda");
    
    private Celula celula;
    private Direcao direcao = Direcao.L;
    
    protected Celula getCelula(){
        return celula;   
    }
    
    
     public WumpusWorld (){
        painel();
     }
     
     public void painel(){
        setTitle("The Wumpus Game by Samuel e Lucas");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Definição do leiaute do painel principal ----------------------------
        setLayout(new GridBagLayout());
        // Objeto que mantém as propriedades do leiaute aplicado ao painel -----

        GridBagConstraints propriedades = new GridBagConstraints();
        
        propriedades.gridwidth = 1; // Reestabelce que o gridwidth padrão é de uma coluna. Observe que o mesmo objeto de propriedades está sendo utilizado para todos os paineis.
        // -- Definição do painel esquerdo -------------------------------------
        pTabuleiro.setVisible(false);
        propriedades.fill = GridBagConstraints.BOTH; // Expansão tanto na horizontal quanto vertical
        propriedades.weightx = 1; //Mantém a largura do painel com 50% da largura da janela
        propriedades.weighty = 0.9; //Mantém a altura do painel com 75% da altura da janela 
        propriedades.gridx = 1; //Posiciona o painel na segunda linha e primeira coluna do gridlayout 
        propriedades.gridy = 2;
        add(pTabuleiro, propriedades);
        
        // -- Definição do painel esquerdo -------------------------------------
        pOpcoes.setVisible(true);
        propriedades.fill = GridBagConstraints.BOTH; // Expansão tanto na horizontal quanto vertical
        propriedades.weightx = 1; //Mantém a largura do painel com 50% da largura da janela
        propriedades.weighty = 0.05; //Mantém a altura do painel com 75% da altura da janela 
        propriedades.gridx = 1; //Posiciona o painel na segunda linha e segunda coluna do gridlayout 
        propriedades.gridy = 0;
        add(pOpcoes, propriedades); 

        pGame.setVisible(false);
        propriedades.fill = GridBagConstraints.BOTH; // Expansão tanto na horizontal quanto vertical
        propriedades.weightx = 1; //Mantém a largura do painel com 50% da largura da janela
        propriedades.weighty = 0.05; //Mantém a altura do painel com 75% da altura da janela 
        propriedades.gridx = 1; //Posiciona o painel na segunda linha e segunda coluna do gridlayout 
        propriedades.gridy = 1;
        add(pGame, propriedades); 
        
        
        this.setVisible(true);
        pTabuleiro.setLayout(new GridLayout(tam, tam));
        janela = new JPanel[tam][tam];
        
        for (int i = 0; i<tam; i++){
            for (int j = 0; j<tam; j++) {
                janela[i][j] = new JPanel();
                janela[i][j].setBackground(new Color(128,128,128));
                janela[i][j].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 2)));
                pTabuleiro.add(janela[i][j]);
            }
        }
        
        
        pOpcoes.add(playB);
        pOpcoes.add(debugB);
        pOpcoes.add(playableB);
        pOpcoes.add(exitB);
        
        playB.addActionListener((ActionListener) this);
        debugB.addActionListener((ActionListener) this);
        playableB.addActionListener((ActionListener) this);
        exitB.addActionListener((ActionListener) this);
     }
     
     
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playB) {
            tab.reset();
            pTabuleiro.setVisible(true);
            pGame.setVisible(true);
            pGame.add(Frente);
            pGame.add(Direita);
            pGame.add(Esquerda);
            
            if(e.getSource() == Frente){
            Jogador jog = new Jogador(tab);
            jog.setCelula(5);
            jog.setAcao(Acao.FRENTE);
            int x = jog.getX();
            int y = jog.getY();
            System.out.println(""+ x);
            
            }   
            else if(e.getSource() == Direita){
            Jogador jog = new Jogador(tab);
            jog.setAcao(Acao.DIREITA);
            jog.setAcao(Acao.FRENTE);
            int x = jog.getX();
            int y = jog.getY();
            System.out.println(""+ x);
        }
            else if(e.getSource() == Esquerda){
            Jogador jog = new Jogador(tab);
            jog.setAcao(Acao.ESQUERDA);
            jog.setAcao(Acao.FRENTE);
            int x = jog.getX();
            int y = jog.getY();
            System.out.println(""+ x);
        }
            
            
        }
        if (e.getSource() == debugB) {
            for (int i = 0; i<tam; i++){
                for (int j = 0; j<tam; j++) {
                    Celula cel = tab.getPosicao(i, j);
                    if(cel.contains(Elemento.OURO)){
                       janela[i][j].setBackground(Color.YELLOW);
                    }
                    else if(cel.contains(Elemento.WUMPUS)){
                        janela[i][j].setBackground(Color.red);
                    }
                    else if(cel.contains(Elemento.WUMPUS2)){
                        janela[i][j].setBackground(Color.cyan);
                    }
                    else if(cel.contains(Elemento.POCO)){
                        janela[i][j].setBackground(Color.black);
                    }
                        else if(cel.contains(Elemento.MADEIRA)){
                    janela[i][j].setBackground(new Color(139, 69, 19));
                    }
                    else if(cel.contains(Elemento.CACADOR)){
                    janela[i][j].setBackground(Color.GREEN);
                    } 
                }   
            }       
        }
        if (e.getSource() == playableB){
            
        }
        if (e.getSource() == exitB){
            System.exit(0);
        }
        
        
    }
    

    public static void main(String[] args) {
        new WumpusWorld();
    }

    private void MoverJogador(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
