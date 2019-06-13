package server;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class Connection {

    static MongoClient mongoClient;
    static MongoDatabase mongoDatabase;


    Connection() {
        mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("miniSkype");
    }

    static MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }

    /**
     * @param collectionName name of collection that your document is in that.
     * @param key key of a field in that document that you want to find it
     * @param value value of that specific key that you gave (previous parameter)
     * @return first document with this specific key value pair given.
     */
    static Document getFirstDocument(String collectionName, String key, String value) {
        return mongoDatabase.getCollection(collectionName).find(eq(key, value)).first();
    }

    /**
     * @param collectionName name of collection that your document is in that.
     * @param key key of a field in that document that you want to find it
     * @param value value of that specific key that you gave (previous parameter)
     * @return all documents with this specific key value pair given.
     *          use this class like : <code>
     *              for(Document doc : getAllDocuments) {
     *                  // working with docs one by one
     *              }
     *          </code>
     */
    static FindIterable<Document> getAllDocuments(String collectionName, String key, String value) {
        return mongoDatabase.getCollection(collectionName).find(eq(key, value)).skip(0);
    }

    static boolean isDocumentInCollection(String collectionName, String key, String value) {
        return mongoDatabase.getCollection(collectionName).find(eq(key, value)).first() != null;
    }

    static Object getValueOfADocumentInCollection(String collectionName, String specificKey, String speceficValue, String key) {
        return mongoDatabase.getCollection(collectionName).find(eq(specificKey, speceficValue)).first().get(key);
    }

}