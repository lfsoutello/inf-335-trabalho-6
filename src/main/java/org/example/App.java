package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @author Luis Soutello
 */
public class App {
    /**
     * f. main – No qual:
     * (i) imprime a lista original de produto,
     * (ii) insere um novo produto,
     * (iii) lista com o novo produto,
     * (iv) altera o valor do produto inserido,
     * (v) lista com valor do produto alterado,
     * (vi) apaga o produto inserido,
     * (vii) lista novamente de modo que seja igual à lista original e
     * (viii) fecha conexão.
     *
     * @param args args
     */
    public static void main(String[] args) {
        final MongoDatabase database = conectar();
        listaProdutos(database);
        insereproduto(database);
        listaProdutos(database);
        alteraValorProduto(database);
        listaProdutos(database);
        apagaProduto(database);
        listaProdutos(database);
    }

    /**
     * Cria a collection via código
     */
    public void criarCollection(final MongoDatabase database) {
        database.createCollection("produtos");
    }

    /**
     * a. concetar — Retorna uma conexão com o banco de dados.
     */
    public static MongoDatabase conectar() {
        final ConnectionString connectionString =
                new ConnectionString("mongodb+srv://leonardo:leonardo@trabalho6.as8ue.mongodb.net/?retryWrites=true&w=majority");
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        final MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("Trabalho6");
    }

    /**
     * b. listaProdutos — Imprime uma lista de todos os produtos
     */
    public static void listaProdutos(final MongoDatabase database) {
        final MongoCollection<Document> col = database.getCollection("produtos");
        // Faz a leitura da coleção
        final FindIterable<Document> fi = col.find();
        final MongoCursor<Document> cursor = fi.iterator();

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * c. insereproduto — Insere um novo produto
     */
    public static void insereproduto(final MongoDatabase database) {
        Document document = new Document();
        document.append("idProduto", "7");
        document.append("nome", "Prod7");
        document.append("descricao", "Bla bla");
        document.append("valor", "666");
        document.append("estado", "Bla bla");
        //Inserting the document into the collection
        database.getCollection("produtos").insertOne(document);
        System.out.println("Documento inserido com sucesso");
    }

    /**
     * d. alteraValorProduto – Altera valor de um produto
     */
    public static void alteraValorProduto(final MongoDatabase database) {
        database.getCollection("produtos").updateOne(Filters.eq("nome", "Prod7"), Updates.set("valor", "777"));
        System.out.println("Document atualizado com sucesso");
    }

    /**
     * e. apagaProduto — Apara um produto
     */
    public static void apagaProduto(final MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("produtos");
        Bson query = Filters.eq("nome", "Prod7");
        try {
            DeleteResult result = collection.deleteOne(query);
            System.out.println("Contagem de documentos deletados: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Não foi possivel deletar o documento: " + me);
        }
    }

}
