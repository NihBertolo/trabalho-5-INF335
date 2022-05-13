package inf335.beans;

import br.unicamp.ic.inf335.beans.AnuncianteBean;
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

public class AnuncianteBeanTest {

    final Double VALOR_SAMSUNG = 4200.00;
    final Double VALOR_MACBOOK = 13000.00;
    final Double VALOR_REDMI_8 = 1500.00;

    AnuncianteBean anuncianteBean;

    ProdutoBean produtoBean;

    AnuncioBean anuncioBean;

    ArrayList<AnuncioBean> anuncios;

    @Test
    public void deveRemoverAnuncio() throws PriceException, MalformedURLException {
        anuncianteBean = CenarioUtils.montarAnunciante(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);
        anuncios = CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);

        Assert.assertEquals(3, anuncianteBean.getAnuncios().size());

        AnuncioBean anuncioARemover = anuncianteBean.getAnuncios().stream().filter(anuncio ->
                anuncio.getProduto().getCodigo().equals("54321")).findAny().orElse(null);

        anuncianteBean.removeAnuncio(anuncianteBean.getAnuncios().indexOf(anuncioARemover));

        Assert.assertEquals(2, anuncianteBean.getAnuncios().size());

        Double[] valores = {VALOR_SAMSUNG, VALOR_REDMI_8};
        for (AnuncioBean anuncio : anuncianteBean.getAnuncios()) {
            Double desconto = anuncio.getDesconto();
            CenarioUtils.CalculoDesconto valorComDesconto = (a, b) -> a - (a * b);

            Assert.assertEquals(
                    valorComDesconto.calcular(
                            valores[anuncianteBean.getAnuncios().indexOf(anuncio)], desconto),
                    anuncio.getValor());
        }
    }

    @Test
    public void deveAdicionarUmNovoAnuncioECalcularMedia() throws PriceException, MalformedURLException {
        anuncianteBean = CenarioUtils.montarAnunciante(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);
        anuncios = CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);

        Assert.assertEquals(3, anuncianteBean.getAnuncios().size());

        produtoBean = new ProdutoBean(
                "32145N",
                "Redmi Note 8",
                "6,9 polegadas",
                VALOR_REDMI_8,
                "Novo"
        );

        ArrayList<URL> urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=redmi12345"),
                        new URL("https://teste2.com.br/photo?=redmi54321"),
                        new URL("https://teste3.com.br/photo?=redmi43215")
                ));
        anuncioBean = new AnuncioBean(produtoBean, urls, 0.1);
        anuncianteBean.addAnuncio(anuncioBean);

        Assert.assertEquals(4, anuncianteBean.getAnuncios().size());

        Double[] valores = {VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8, VALOR_REDMI_8};
        Double somaValoresComDesconto = 0.0;
        for (AnuncioBean anuncio : anuncianteBean.getAnuncios()) {
            Double desconto = anuncio.getDesconto();
            CenarioUtils.CalculoDesconto valorComDesconto = (a, b) -> a - (a * b);

            somaValoresComDesconto += valorComDesconto.calcular(
                    valores[anuncianteBean.getAnuncios().indexOf(anuncio)],
            anuncio.getDesconto());
        }
        somaValoresComDesconto = somaValoresComDesconto/anuncianteBean.getAnuncios().size();

        Assert.assertEquals(somaValoresComDesconto, anuncianteBean.valorMedioAnuncios());
    }

    @Test
    public void deveLancarExcessaoAoCalcularMediaSemAnuncios() throws PriceException, MalformedURLException {
        anuncianteBean = CenarioUtils.montarAnunciante(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);
        anuncios = CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);

        final String MSG_ERRO = "Não é possível calcular média sem anuncios.";
        Exception excecao = Assert.assertThrows(ArithmeticException.class, ()-> {
           anuncianteBean.removeAnuncio(2);
           anuncianteBean.removeAnuncio(1);
           anuncianteBean.removeAnuncio(0);

           anuncianteBean.valorMedioAnuncios();
        });

        Assert.assertEquals(MSG_ERRO, excecao.getMessage());
    }

    @Test
    public void deveMontarEAlterarUmAnuncianteDeAcordoComInformacoesEsperadas() throws PriceException, MalformedURLException {
        anuncianteBean = CenarioUtils.montarAnunciante(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);
        anuncios = CenarioUtils.montarAnuncios(VALOR_SAMSUNG, VALOR_MACBOOK, VALOR_REDMI_8);

        Assert.assertEquals("Davy Jones", anuncianteBean.getNome());
        Assert.assertEquals("809.213.452-32", anuncianteBean.getCPF());

        anuncios.add(new AnuncioBean(
                produtoBean,
                new ArrayList<URL>(Arrays.asList(
                        new URL("https://teste1.com.br/photo?=12345"),
                        new URL("https://teste2.com.br/photo?=54321"),
                        new URL("https://teste3.com.br/photo?=43215")
                )),
                0.20));

        anuncianteBean.setNome("Jack Sparrow");
        anuncianteBean.setCPF("151.214.621-XX");
        anuncianteBean.setAnuncios(anuncios);

        Assert.assertEquals("Jack Sparrow", anuncianteBean.getNome());
        Assert.assertEquals("151.214.621-XX", anuncianteBean.getCPF());
        Assert.assertEquals(anuncios, anuncianteBean.getAnuncios());

        anuncianteBean = new AnuncianteBean();
        anuncios = new ArrayList<>();

        Assert.assertEquals("", anuncianteBean.getNome());
        Assert.assertEquals("", anuncianteBean.getCPF());
        Assert.assertEquals(anuncios, anuncianteBean.getAnuncios());
    }
}
