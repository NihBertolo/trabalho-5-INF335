package inf335.beans;


import br.unicamp.ic.inf335.beans.ProdutoBean;
import br.unicamp.ic.inf335.exceptions.PriceException;
import inf335.utils.CenarioUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class ProdutoBeanTest {

    ArrayList<ProdutoBean> produtos;

    @Test
    public void deveOrdernarListaDeProdutosPorValor() throws PriceException {
        produtos = CenarioUtils.montarProdutos();

        Assert.assertEquals(4, produtos.size());

        Collections.sort(produtos);

        double valorAux = 0.0;
        for (ProdutoBean produto : produtos) {
            if (produtos.indexOf(produto) > 0) {
                Assert.assertTrue(valorAux <= produto.getValor());
            }
            valorAux = produto.getValor();
        }
    }

    @Test
    public void deveLancarExcessaoQuandoPrecoForNegativo() throws PriceException {
        produtos = CenarioUtils.montarProdutos();

        AtomicReference<ProdutoBean> produto = new AtomicReference<>(produtos.get(0));
        final String MSG_ERRO = "Valor nao deve ser negativo.";

        Exception excecao = Assert.assertThrows(PriceException.class, () -> {
            produto.get().setValor(-230.00);
        });

        Assert.assertEquals(MSG_ERRO, excecao.getMessage());

        excecao = Assert.assertThrows(PriceException.class, () -> {
            produto.set(new ProdutoBean(
                    "22222",
                    "Xbox",
                    "Tela Amoled",
                    -2402.22,
                    "Novo"
            ));
        });

        Assert.assertSame(MSG_ERRO, excecao.getMessage());
    }

    @Test
    public void deveMontarEAlterarUmProdutoDeAcordoComInformacoesEsperadas() throws PriceException {
        final String codigo = "T3ST_12345";
        final String nome = "Pendrive 16GB";
        final String descricao = "Kingston";
        final Double valor = 42.90;
        final String estado = "Seminovo";

        ProdutoBean produtoBean = CenarioUtils.montarProdutoUnico(codigo, nome, descricao, valor, estado);

        Assert.assertEquals(codigo, produtoBean.getCodigo());
        Assert.assertEquals(nome, produtoBean.getNome());
        Assert.assertEquals(descricao, produtoBean.getDescricao());
        Assert.assertEquals(valor, produtoBean.getValor());
        Assert.assertEquals(estado, produtoBean.getEstado());
        Assert.assertEquals(1L, ProdutoBean.getSerialversionuid());

        final String codigoNovo = "T3ST_54321";
        final String nomeNovo = "Pendrive 32GB";
        final String descricaoNova = "Sandisk";
        final Double valorNovo = 60.90;
        final String estadoNovo = "Novo";

        produtoBean.setCodigo(codigoNovo);
        produtoBean.setNome(nomeNovo);
        produtoBean.setDescricao(descricaoNova);
        produtoBean.setValor(valorNovo);
        produtoBean.setEstado(estadoNovo);

        Assert.assertEquals(codigoNovo, produtoBean.getCodigo());
        Assert.assertEquals(nomeNovo, produtoBean.getNome());
        Assert.assertEquals(descricaoNova, produtoBean.getDescricao());
        Assert.assertEquals(valorNovo, produtoBean.getValor());
        Assert.assertEquals(estadoNovo, produtoBean.getEstado());
    }
}
