package service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Produto;
import org.bson.Document;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    public void salvarProduto(
            Produto produto,
            Connection connection,
            MongoClient mongoClient)
            throws SQLException {
        if (connection != null) {
            try (connection) {
                Statement stmt = connection.createStatement();

                String query = "INSERT INTO PRODUTOS VALUES ('?id','?nome','?descricao','?valor','?estado');";
                query.replace("?id", produto.getId());
                query.replace("?nome", produto.getNome());
                query.replace("?descricao", produto.getDescricao());
                query.replace("?valor", produto.getValor());
                query.replace("?estado", produto.getEstado());
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            MongoDatabase database = mongoClient.getDatabase("INF335");
            Document document = new Document();
            document.append("_id", produto.getId());
            document.append("nome", produto.getNome());
            document.append("descricao", produto.getDescricao());
            document.append("valor", produto.getValor());
            document.append("estado", produto.getEstado());
            database.getCollection("produtos").insertOne(document);
        }
    }

    public List<Produto> listarProdutos(Connection connection, MongoClient mongoClient) throws SQLException {
        ArrayList<Produto> produtos = new ArrayList<>();
        if (connection != null) {
            try (connection) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUTOS");

                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getString("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getString("valor"),
                            rs.getString("estado")
                    );
                    produtos.add(produto);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            MongoCursor<Document> cursor = mongoClient
                    .getDatabase("INF335")
                    .getCollection("PRODUTOS")
                    .find(new BasicDBObject())
                    .iterator();


            BasicDBList produtosDBList = (BasicDBList) cursor.next().get("produtos");

            for (int i = 0; i < produtosDBList.size(); i++) {
                BasicDBObject produtoObj = (BasicDBObject) produtosDBList.get(i);
                String id = produtoObj.getString("_id");
                String nome = produtoObj.getString("nome");
                String descricao = produtoObj.getString("descricao");
                String valor = produtoObj.getString("valor");
                String estado = produtoObj.getString("estado");

                Produto produto = new Produto(id, nome, descricao, valor, estado);

                produtos.add(produto);
            }
         }
        return produtos;
    }

    public void alterarValorProduto(
            String produtoId,
            String valor,
            Connection connection,
            MongoClient mongoClient) {

        if (connection != null) {
            try (connection) {
                Statement stmt = connection.createStatement();

                String query = "UPDATE PRODUTOS SET VALOR = '?valor' WHERE id = '?id';";
                query.replace("?id", produtoId);
                query.replace("?valor", valor);
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
