package server;

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
                PreparedStatement pstmt = c.prepareStatement("Select nome FROM ingredientes WHERE nome ilike ?");
                pstmt.setString(1, nome);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    mensagem = "Ja existe este ingrediente, adicione outro!";
                } else {
                    System.out.println("4");
                    createTableQuery = "INSERT INTO ingredientes(nome) VALUES(?)";
                    PreparedStatement stmt = c.prepareStatement(createTableQuery);
                    stmt.setString(1, nome);
                    stmt.execute();
                    mensagem = "Ingrediente adicionado com sucesso!";
                    stmt.close();
                }
            } else {
                mensagem = "Base dados desligada, em primeiro lugar ligue-a!";
            }
        } catch (Exception e) {
            mensagem = "Erro:" + e.getMessage();
        }
        return mensagem;
    }

    @WebMethod
    public String editarIngrediente(@WebParam(name = "nome") String nome, @WebParam(name = "novoNome") String novoNome) {
        String mensagem = "";
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                System.out.println("Base dados conetada, estou a adicionar...");
                PreparedStatement pstmt = c.prepareStatement("Select nome FROM ingredientes WHERE nome ilike ?");
                pstmt.setString(1, novoNome);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    mensagem = "Este ingrediente j� existe, adicione outro!";
                } else {
                    createTableQuery = "Update ingredientes SET nome = ? where nome ilike '" + nome + "' ;";
                    PreparedStatement stmt = c.prepareStatement(createTableQuery);
                    stmt.setString(1, novoNome);
                    stmt.execute();
                    mensagem = "Ingrediente alterado com sucesso!";
                    stmt.close();
                }

            } else {
                mensagem = "Base dados desligada, em primeiro lugar ligue-a!";
            }
        } catch (Exception e) {
            mensagem = "Erro a: " + e.getMessage();
        }
        return mensagem;
    }

    @WebMethod
    public String removerIngrediente(@WebParam(name = "nome") String nome) {
        String mensagem = null;
        try {
            if ((c = bd.conectarPostsgresql()) != null) {
                System.out.println("Base dados contetada, a remover..");
                stmt = c.createStatement();
                createTableQuery = "DELETE FROM ingredientes WHERE nome ilike '" + nome + "';";

                int r = stmt.executeUpdate(createTableQuery);
                if (r == 0) {
                    mensagem = "Erro ao remover, tente novamente...";
                } else if (r == 1) {
                    mensagem = "Ingrediente removido com sucesso!";
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
