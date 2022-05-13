package inf335.beans;


import br.unicamp.ic.inf335.beans.AnuncioBean;
import br.unicamp.ic.inf335.beans.ProdutoBean;
import br.unicamp.ic.inf335.exceptions.PriceException;
import inf335.utils.CenarioUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AnuncioBeanTest {

    final Double VALOR_SAMSUNG = 4200.00;
    final Double VALOR_MACBOOK = 13000.00;
    final Double VALOR_REDMI_8 = 1500.00;

    AnuncioBean anuncioBean = new AnuncioBean();

    ProdutoBean produtoBean = new ProdutoBean();

    ArrayList<AnuncioBean> anuncios;

    ArrayList<Double> valoresSemDesconto = new ArrayList<>(
            Arrays.asList(
                    VALOR_SAMSUNG,
                    VALOR_MACBOOK,
                    VALOR_REDMI_8
            )
    );


    @Test
    public void deveRetornarValorFinalComDesconto() throws PriceException, MalformedURLException {
        anuncios = new ArrayList<>(CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8));

        for (AnuncioBean anuncio : anuncios) {
            Double valorSemDesconto = valoresSemDesconto.get(anuncios.indexOf(anuncio));
            Double desconto = anuncio.getDesconto();
            CenarioUtils.CalculoDesconto valorComDesconto = (a, b) -> a - (a * b);

            Assert.assertEquals(valorComDesconto.calcular(valorSemDesconto, desconto), anuncio.getValor());
        }
    }

    @Test
    public void deveLancarExcecaoAoInserirDescontoForaDoIntervalo() throws PriceException, MalformedURLException {
        anuncios = new ArrayList<>(CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8));

        final String MSG_ERRO = "Valor do desconto deve estar entre 0 e 1.";

        produtoBean = new ProdutoBean(
                "INF335",
                "Samsung SmartTV com Desconto menor que 0%",
                "45 polegadas",
                VALOR_SAMSUNG,
                "Semi-Novo"
        );

        ArrayList<URL> urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=12345"),
                        new URL("https://teste2.com.br/photo?=54321"),
                        new URL("https://teste3.com.br/photo?=43215")
                )
        );
        ArrayList<URL> finalUrlsSamsung = urls;

        Exception excecao = Assert.assertThrows(PriceException.class, () -> {
            anuncioBean = new AnuncioBean(produtoBean, finalUrlsSamsung, -0.01);
        });
        Assert.assertEquals(MSG_ERRO, excecao.getMessage());

        produtoBean = new ProdutoBean(
                "INF335",
                "MacBook Pro com Desconto maior que 100%",
                "Intel i5",
                VALOR_MACBOOK,
                "Semi-Novo"
        );

        urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=mac12345"),
                        new URL("https://teste2.com.br/photo?=mac54321"),
                        new URL("https://teste3.com.br/photo?=mac43215")
                )
        );
        ArrayList<URL> finalUrlsMacBook = urls;

        excecao = Assert.assertThrows(PriceException.class, () -> {
            anuncioBean = new AnuncioBean(produtoBean, finalUrlsMacBook, 1.2);
        });
        Assert.assertEquals(MSG_ERRO, excecao.getMessage());
    }

    @Test
    public void deveMontarEAlterarUnuncioUnicoDeAcordoComInformacoesEsperadas() throws PriceException, MalformedURLException {
        final String codigo = "INF335";
        final String nome = "MacBook Pro";
        final String descricao = "Intel i5";
        final String estado = "Seminovo";

        ArrayList<URL> urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=12345"),
                        new URL("https://teste2.com.br/photo?=54321"),
                        new URL("https://teste3.com.br/photo?=43215")
                )
        );

        produtoBean = new ProdutoBean(
                codigo,
                nome,
                descricao,
                VALOR_MACBOOK,
                estado
        );
        anuncioBean = new AnuncioBean(produtoBean, urls, 0.28);

        CenarioUtils.CalculoDesconto valorComDesconto = (a, b) -> a - (a * b);

        Assert.assertEquals(produtoBean, anuncioBean.getProduto());
        Assert.assertEquals((Double) 0.28, anuncioBean.getDesconto());
        Assert.assertEquals(urls, anuncioBean.getFotosUrl());
        Assert.assertEquals(1L, AnuncioBean.getSerialversionuid());
        Assert.assertEquals(valorComDesconto.calcular(VALOR_MACBOOK, 0.28), anuncioBean.getValor());

        final String codigoNovo = "INF335A";
        final String nomeNovo = "MacBook Pro";
        final String descricaoNovo = "Intel i7";
        final String estadoNovo = "Novo";

        urls.add(new URL("https://teste3.com.br/photo?=43215NOVA"));

        produtoBean = new ProdutoBean(
                codigoNovo,
                nomeNovo,
                descricaoNovo,
                VALOR_MACBOOK,
                estadoNovo
        );

        anuncioBean.setDesconto(0.08);
        anuncioBean.setFotosUrl(urls);
        anuncioBean.setProduto(produtoBean);

        Assert.assertEquals(produtoBean, anuncioBean.getProduto());
        Assert.assertEquals((Double) 0.08, anuncioBean.getDesconto());
        Assert.assertEquals(urls, anuncioBean.getFotosUrl());
        Assert.assertEquals(1L, AnuncioBean.getSerialversionuid());
        Assert.assertEquals(valorComDesconto.calcular(VALOR_MACBOOK, 0.08), anuncioBean.getValor());
    }
}
