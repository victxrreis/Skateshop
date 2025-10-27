package br.com.skateshop.data.repository

import br.com.skateshop.R
import br.com.skateshop.data.model.Produto

object DadosIniciais {

    private val produtos = listOf(
        Produto("s1", "Shape Element", "Shape profissional Element 8.0 pol.", 250.0, R.drawable.shape_element, 10, "shape", "8.0 pol"),
        Produto("s2", "Shape Santa Cruz", "Shape Santa Cruz 8.25 pol.", 300.0, R.drawable.shape_santa_cruz, 5, "shape", "8.25 pol"),
        Produto("r1", "Roda Spitfire 52mm", "Roda Spitfire F4 52mm 99DU.", 280.0, R.drawable.roda_spitfire_52mm, 15, "rodas", "52mm"),
        Produto("r2", "Roda Bones 54mm", "Roda Bones STF 54mm 103A.", 320.0, R.drawable.roda_bones_54mm, 8, "rodas", "54mm"),
        Produto("t1", "Truck Independent 139mm", "Truck Indepedent Prata 139mm.", 450.0, R.drawable.truck_independent, 7, "truck", "139mm"),
        Produto("c1", "Camiseta Thrasher", "Camiseta Thrasher Logo Preta.", 150.0, R.drawable.camiseta_thrasher, 20, "roupas", "G"),
        Produto("o1", "Rolamento Bones Ceramic", "Rolamento Bones Ceramic Super Reds.", 220.0, R.drawable.rolamento_bones_ceramic, 30, "rolamentos", "Ceramic")
    )

    fun getProdutosIniciais(): List<Produto> = produtos
    fun getCategorias(): List<String> = listOf("shape", "truck", "rolamentos", "rodas", "roupas", "outros")
}