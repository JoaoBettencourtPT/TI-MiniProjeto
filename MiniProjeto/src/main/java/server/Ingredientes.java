package server;

import client.FormIngredients;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class Ingredientes {

    BaseDados bd = new BaseDados();
    Statement stmt = null;
    String createTableQuery = null;
    Connection c = null;

    @WebMethod
    public String adicionarIngrediente(@WebParam(name = "nome") String nome) {
        String mensagem = "";
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                System.out.println("Base dados conetada, estou a adicionar...");
                createTableQuery = "INSERT INTO ingredientes(nome) VALUES(?)";
                PreparedStatement stmt = c.prepareStatement(createTableQuery);
                stmt.setString(1, nome);
                stmt.execute();
                mensagem = "Inserido com sucesso o ingrediente: " + nome;
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
    public String removerIngrediente(@WebParam(name = "nomeIngrediente") String nomeIngrediente) {
        String mensagem = "";
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                stmt = c.createStatement();
                String sql = "DELETE FROM ingredientes WHERE nome ilike '" + nomeIngrediente + "'";
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
    public String[] verTodosOsIngredientes() {
        int size = 0, i = 0;
        String[] ingredientes = null;
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                PreparedStatement pstmt = c.prepareStatement("SELECT * FROM ingredientes;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = pstmt.executeQuery();
                if (rs != null) {
                    rs.last();
                    size = rs.getRow();
                    rs.beforeFirst();
                }
                ingredientes = new String[size];
                while (rs.next()) {
                    ingredientes[i] = rs.getString("nome");
                    i++;
                }
                pstmt.close();
            }
        } catch (Exception e) {
            return new String[]{e.getMessage()};
        }

        return ingredientes;
    }

}
