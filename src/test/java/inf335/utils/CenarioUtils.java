package inf335.utils;

import br.unicamp.ic.inf335.beans.AnuncianteBean;
import br.unicamp.ic.inf335.beans.AnuncioBean;
import br.unicamp.ic.inf335.beans.ProdutoBean;
import br.unicamp.ic.inf335.exceptions.PriceException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class CenarioUtils {

    public static ProdutoBean montarProdutoUnico(
            String codigo,
            String nome,
            String descricao,
            Double valor,
            String estado
    ) throws PriceException {
        return new ProdutoBean(codigo, nome, descricao, valor, estado);
    }

    public static ArrayList<ProdutoBean> montarProdutos() throws PriceException {
        ArrayList<ProdutoBean> produtos = new ArrayList<>();

        ProdutoBean produto = new ProdutoBean(
                "12345",
                "Televisão",
                "45 polegadas",
                2500.00,
                "Novo"
        );
        produtos.add(produto);

        produto = new ProdutoBean(
                "54321",
                "Samsung s22",
                "Tela Amoled",
                8920.00,
                "Novo"
        );
        produtos.add(produto);

        produto = new ProdutoBean(
                "54321A",
                "Samsung s22",
                "Tela Amoled",
                8920.00,
                "Novo"
        );
        produtos.add(produto);

        produto = new ProdutoBean(
                "34215",
                "Acer i5",
                "Setima Geração",
                2700.00,
                "Semi-Novo"
        );
        produtos.add(produto);

        return produtos;
    }

    public static ArrayList<AnuncioBean> montarAnuncios(
            Double valor1,
            Double valor2,
            Double valor3
    ) throws PriceException, MalformedURLException {
        ArrayList<AnuncioBean> anuncios = new ArrayList<>();
        ProdutoBean produtoBean = new ProdutoBean(
                "12345",
                "Samsung SmartTV",
                "45 polegadas",
                valor1,
                "Novo"
        );

        ArrayList<URL> urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=12345"),
                        new URL("https://teste2.com.br/photo?=54321"),
                        new URL("https://teste3.com.br/photo?=43215")
                ));

        AnuncioBean anuncioBean = new AnuncioBean(produtoBean, urls, 0.2);
        anuncios.add(anuncioBean);

        produtoBean = new ProdutoBean(
                "54321",
                "MacBook Pro",
                "Intel i5",
                valor2,
                "Novo"
        );

        urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=mac12345"),
                        new URL("https://teste2.com.br/photo?=mac54321"),
                        new URL("https://teste3.com.br/photo?=mac43215")
                ));

        anuncioBean = new AnuncioBean(produtoBean, urls, 0.3);
        anuncios.add(anuncioBean);

        produtoBean = new ProdutoBean(
                "32145",
                "Redmi Note 8",
                "6,9 polegadas",
                valor3,
                "Semi-Novo"
        );

        urls = new ArrayList<>(
                Arrays.asList(
                        new URL("https://teste1.com.br/photo?=redmi12345"),
                        new URL("https://teste2.com.br/photo?=redmi54321"),
                        new URL("https://teste3.com.br/photo?=redmi43215")
                ));
        anuncioBean = new AnuncioBean(produtoBean, urls, 0.65);
        anuncios.add(anuncioBean);

        return anuncios;
    }

    public static AnuncianteBean montarAnunciante(
            Double valor1,
            Double valor2,
            Double valor3
    ) throws PriceException, MalformedURLException {
        ArrayList<AnuncioBean> anuncios = montarAnuncios(valor1, valor2, valor3);
        return new AnuncianteBean("Davy Jones", "809.213.452-32", anuncios);
    }

    public interface CalculoDesconto {
        Double calcular(Double a, Double b);
    }
}
