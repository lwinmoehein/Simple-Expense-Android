package lab.justonebyte.moneysubuu.model

data class AppList(
   val apps:List<App>,
   val latestAppNumber:Int
)
data class App(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val playstoreUrl: String
)