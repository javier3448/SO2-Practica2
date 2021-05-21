/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problema2;

/**
 *
 * @author Carlos
 */
public class Cliente {
    int id;
  
    public Cliente(int id){
        this.id = id;
        System.out.println("Cliente Generado: " + this.id);
    }
            

    
    
    public int getId(){
        return this.id;
    }
}
