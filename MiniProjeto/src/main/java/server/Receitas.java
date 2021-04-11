/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;

/**
 *
 * @author Asus
 */

@WebService
public class Receitas {
    
    BaseDados bd = new BaseDados();
    Statement stmt = null;
    String createTableQuery = null;
    Connection c = null;
    
    @WebMethod
    public String adicionarReceitas(@WebParam(name = "nomeReceitas") String nomeReceitas, @WebParam(name = "instrucoes") String instrucoes) {
        String mensagem = "";
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                System.out.println("Base dados conetada!");
<<<<<<< HEAD
                createTableQuery = "INSERT INTO ingredientes(nome) VALUES (?);";
=======
                createTableQuery = "INSERT INTO ingredientes(nome, instrucoes) VALUES (?,?);";
>>>>>>> 474a32f90914a1a03c7dc13f5966661e1ad225e9
                PreparedStatement stmt = c.prepareStatement(createTableQuery);
                stmt.setString(1, nomeReceitas);
                stmt.setString(2, instrucoes);
                stmt.execute(); // Executa o PreparedStatement com o SQL já incluso e os valoes Setados
                mensagem = "Inserido com sucesso a receita: " + nomeReceitas;
                stmt.close();
            } else {
                mensagem = "Base dados desligada, em primeiro lugar ligue-a!";
            }

        } catch (Exception e) {
            mensagem = "Erro a: " + e.getMessage();
        }
        return mensagem;
    }
    
    @WebMethod
    public String removerReceita(@WebParam(name = "nomeReceita") String nomeReceita) {
        String mensagem = "";
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                stmt = c.createStatement();
                String sql = "DELETE FROM ingredientes WHERE nome ilike '" + nomeReceita + "'";
                int i = stmt.executeUpdate(sql);
                if (i == 0) {
                    mensagem = "Erro ao remover!";
                } else {
                    mensagem = "Removido com sucesos!";
                }
                stmt.close();
                c.close();
            } else {
                mensagem = "Base dados desligada, em primeiro lugar ligue-a!";
            }
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
        return mensagem;
    }

    @WebMethod
    public String adicionarIngrediente(@WebParam(name = "receita") String receita,@WebParam(name = "ingrediente") String ingrediente){
        int id, id_R;
        try{
            if((c = bd.conectarPostsgresql()) != null){
                PreparedStatement psmt = c.prepareStatement("SELECT id FROM ingredientes WHERE nome = ?");
                psmt.setString(1, ingrediente);
                ResultSet rs = psmt.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                    PreparedStatement psmt_R = c.prepareStatement("SELECT id FROM receitas WHERE nome = ?");
                    psmt_R.setString(1, receita);
                    ResultSet rs_R = psmt_R.executeQuery();
                    if(rs_R.next()){
                        id_R = rs_R.getInt("id");
                        PreparedStatement psmt_F = c.prepareStatement("INSERT INTO ingredientes_receitas(receita_id,ingrediente_id) VALUES(?,?)");
                        psmt_F.setInt(1,id_R);
                        psmt_F.setInt(2,id);
                        psmt_F.execute();
                        return "Ingrediente adicionado à receita com sucesso!";
                    }
                }
            }
            return "Base de dados desligada!";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @WebMethod
    public String removerIngrediente(@WebParam(name = "receita") String receita,@WebParam(name = "ingrediente") String ingrediente){
        int id, id_R;
        try{
            if((c = bd.conectarPostsgresql()) != null){
                PreparedStatement psmt = c.prepareStatement("SELECT id FROM ingredientes WHERE nome = ?");
                psmt.setString(1, ingrediente);
                ResultSet rs = psmt.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                    PreparedStatement psmt_R = c.prepareStatement("SELECT id FROM receitas WHERE nome = ?");
                    psmt_R.setString(1, receita);
                    ResultSet rs_R = psmt_R.executeQuery();
                    if(rs_R.next()){
                        id_R = rs_R.getInt("id");
                        PreparedStatement psmt_F = c.prepareStatement("DELETE FROM ingredientes_receitas WHERE receita_id = ? AND ingrediente_id = ?");
                        psmt_F.setInt(1,id_R);
                        psmt_F.setInt(2,id);
                        psmt_F.execute();
                        return "Ingrediente removida da receita com sucesso!";
                    }
                }
            }
            return "Base de dados desligada!";
        }catch(Exception e){
            return e.getMessage();
        }
    }


    private void listarIngredientes(Receita receita){
        int id_R;

        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                PreparedStatement psmt_R = c.prepareStatement("SELECT id FROM receitas WHERE nome = ?");
                psmt_R.setString(1, receita.getNome());
                ResultSet rs_R = psmt_R.executeQuery();
                if (rs_R.next()) {
                    id_R = rs_R.getInt("id");
                    PreparedStatement pstmt = c.prepareStatement("SELECT i.nome FROM ingredientes_receitas AS ir INNER JOIN ingredientes i ON i.id = ir.ingrediente_id WHERE ir.receita_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    pstmt.setInt(1, id_R);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        receita.addIngrediente(rs.getString("nome"));
                    }
                    pstmt.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @WebMethod
    public ArrayList<Receita> verTodasAsReceitas() {
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                PreparedStatement pstmt = c.prepareStatement("SELECT * FROM receitas;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
<<<<<<< HEAD
                    Receita temp = new Receita(rs.getString("nome"), rs.getString("instrucoes"));
                    listarIngredientes(temp);
                    receitas.add(temp);
=======
                    ingredientes[i] = rs.getString("nome");
                    ingredientes[i] += rs.getString("instrucoes");
                    i++;
>>>>>>> 474a32f90914a1a03c7dc13f5966661e1ad225e9
                }
                pstmt.close();
            }
        } catch (Exception e) {
            return null;
        }

        return receitas;
    }
    
    
}
