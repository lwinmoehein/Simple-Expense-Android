package lab.justonebyte.simpleexpense.ui.components

import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.TransactionType

sealed class TransactionTypeTab(override val value:Int, override val name:Int, val transactionType: TransactionType): OptionItem {
    object EXPENSE : TransactionTypeTab(1, R.string.expense, TransactionType.Expense)
    object INCOME : TransactionTypeTab(2, R.string.income, TransactionType.Income)
}
