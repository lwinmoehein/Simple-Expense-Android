package lab.justonebyte.moneysubuu.api

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import lab.justonebyte.moneysubuu.model.Transaction

val TAG = "TransactionService"

class TransactionService {
    object Network {
        fun uploadTransactions(transactions: List<Transaction>){
            val db = Firebase.firestore

            val dtoTransactions = transactions.map { Transaction.Mapper.mapToDTO(it) }

            for(t in dtoTransactions){
                db.collection("transactions")
                    .document(t.global_id)
                    .set(t)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added ")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }
    }
}