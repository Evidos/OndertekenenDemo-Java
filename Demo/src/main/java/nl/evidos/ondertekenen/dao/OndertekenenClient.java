package nl.evidos.ondertekenen.dao;

import nl.evidos.ondertekenen.objects.*;

import java.io.File;

/**
 * A Client which interfaces with the Signhost API (in dutch branded Ondertekenen).
 *
 * Description from SignHost:
 * <pre>
 * Signhost.com is a service that makes it possible to digitally sign, seal or deliver
 * your documents. (http://www.signhost.com) Documents can be signed directly from your
 * webportal or by sending a signing request by email or other message to the end-user.
 *
 * In some countries signhost is branded by a locale domain, like ondertekenen.nl in
 * The Netherlands. In this document the signhost.com domain is replaceable by the
 * local promoted domains like ondertekenen.nl.
 *
 * During the signing process of the end-user, different identity verification methods
 * can be used to include in the digital signature ie; email, SMS, scribble signature
 * or authentication mechanisms like Google, LinkedIn, and national eID like DigiD(NL)
 * or eHerkenning(NL).
 *
 * <b>Note:</b> All communication is required to be done over SSL.
 * </pre>
 *
 * @author Yuri Meiburg
 *
 */
public interface OndertekenenClient {

    /**
     * Execute REST Call to get a Signed document for a given document ID
     * @param fileID The Document ID
     * @param sendSignRequest If {@code true} the Sender and Signer will also receive a signed document and receipt by email.
     * @return A ModelObject containing a signed document with the given document ID
     */
    Document getSignedDocument(String fileID, boolean sendSignRequest);

    /**
     * Execute REST Call to get a Signed document for a given document ID
     * @param documentID The Document ID
     * @param sendSignRequest If {@code true} the Sender and Signer will also receive a signed document and receipt by email.
     * @return A ModelObject containing the receipt for the given document ID
     */
    Receipt getReceipt(String documentID, boolean sendSignRequest);

    /**
     * Execute REST Call to get a transaction by a transaction id.
     * @param transactionId The transaction ID
     * @return A ModelObject containing the transaction for the given transaction ID
     */
    Transaction getTransaction(String transactionId);

    /**
     * Execute REST Call to delete a transaction by a transaction id.
     * @param transactionId The transaction ID
     * @param sendNotification True if notifications should be sent of the cancelling.
     * @param reason The reason of the cancel.
     * @return The transaction that was removed
     */
    Transaction deleteTransaction(String transactionId, boolean sendNotification, String reason);

    /**
     * Execute REST Call to delete a Transaction
     * @param transaction The transaction to be deleted
     * @param sendNotification True if notifications should be sent of the cancelling.
     * @param reason The reason of the cancel.
     * @return The transaction that was removed
     */
    Transaction deleteTransaction(Transaction transaction, boolean sendNotification, String reason);

    /**
     * Execute REST Call to create a new transaction
     * @param transaction The data object containing all parameters to create a new transaction.
     * @return A new Transaction ID
     */
    Transaction createTransaction(Transaction transaction);

    /**
     * Execute REST Call to upload a PDF
     * @param transaction The transaction for which the file should be appended
     * @param file The location of the file to upload
     * @return Response object containing null upon success, and an ErrorMessage if something went wrong.
     */
    void uploadFile(Transaction transaction, File file);
}
