package io.github.matheuscarv69.service.impl;

import io.github.matheuscarv69.domain.entity.FormSocial;
import io.github.matheuscarv69.domain.entity.othersEntity.*;
import io.github.matheuscarv69.domain.repository.*;
import io.github.matheuscarv69.exceptions.*;
import io.github.matheuscarv69.rest.dto.FormSocialDTO;
import io.github.matheuscarv69.service.FormSocialService;
import io.github.matheuscarv69.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormSocialServiceImpl implements FormSocialService {

    private final FormSocialRepository repository;
    private final EstadoCivilRepository estadoCivilRepository;
    private final EscolaridadeRepository escolaridadeRepository;
    private final ResidenciaRepository residenciaRepository;
    private final GrauParentescoRepository grauParentescoRepository;
    private final BeneficioRepository beneficioRepository;
    private final ProgramaSocialRepository programaSocialRepository;
    private final DoencaCronicaRepository doencaCronicaRepository;
    private final DeficienteFamiliaRepository deficienteFamiliaRepository;
    private final AcompMedicoRepository acompMedicoRepository;
    private final SuicidioFamiliaRepository suicidioFamiliaRepository;

    @Override
    @Transactional
    public FormSocial salvar(FormSocialDTO dto) {

        FormSocial formSocial = new FormSocial();

        formSocial.setNome(dto.getNome());
        formSocial.setTelefone(dto.getTelefone());
        formSocial.setEmail(dto.getEmail());
        formSocial.setDataEntrevista(new Date());

        Date date = DateUtil.formataStringData(dto.getDataNascimento());
        formSocial.setDataNascimento(date);

        formSocial.setFuncaoExerc(dto.getFuncaoExerc());
        formSocial.setTempoFuncaoExerc(dto.getTempoFuncaoExerc());

        //estadoCivil
        formSocial.setEstadoCivil(persistEstadoCivil(dto));

        // escolaridade
        formSocial.setEscolaridade(persistEscolaridade(dto));

        // grauParentesco
        List<GrauParentesco> listParentescos = convertIndexGrauParentesco(dto);

        // numero de pessoas familia
        formSocial.setNumeroPessoasFam(dto.getNumeroPessoasFam());

        // residencia
        formSocial.setResidencia(persistResidencia(dto));

        // beneficios
        List<Beneficio> listBeneficios = convertIndexBeneficio(dto);

        // programas sociais
        List<ProgramaSocial> listProgramas = convertIndexProgramaSocial(dto);

        // doenca cronicas
        List<DoencaCronica> listDoenca = convertIndexDoencaCronica(dto);

        // deficiente na familia
        formSocial.setDeficienteFamilia(persistDeficienteFamilia(dto));

        // acompanhamento medico
        formSocial.setAcompMedico(persistAcompMedico(dto));

        // suicidio familia
        formSocial.setSuicidioFamilia(persistSuicidioFam(dto));
//        persistSuicidioFam(dto);


        ///

        repository.save(formSocial);

        // salva os ids nas tabelas associativas
        formSocial.getGrauParentescos().addAll(listParentescos);
        formSocial.getBeneficios().addAll(listBeneficios);
        formSocial.getProgramasSociais().addAll(listProgramas);
        formSocial.getDoencaCronicas().addAll(listDoenca);
        repository.save(formSocial);

        return formSocial;


//        formSocial.setSuicidioFamilia(Decisao.getDecisaoCode(Integer.parseInt(dto.getSuicidioFamilia())));
//        if (Integer.parseInt(dto.getSuicidioFamilia()) == 1 && dto.getSuicidioGrauParentesco().isEmpty()) {
//            throw new RegraNegocioException("Campo Grau de Parentesco do Suicidio da Família é obrigatório.");
//        } else if (Integer.parseInt(dto.getSuicidioFamilia()) == 2 && dto.getSuicidioGrauParentesco().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Suícidio Familia deve estar com o (Sim == 1) selecionado");
//        } else if(Integer.parseInt(dto.getSuicidioFamilia()) == 2 && dto.getSuicidioGrauParentesco().isEmpty()){
//            formSocial.setSuicidioGrauParentesco(GrauParentesco.NENHUM_PARENTE);
//            System.out.println("Campo nao e grau vazio");
//        }else{
//            formSocial.setSuicidioGrauParentesco(GrauParentesco.getGrauParentescoCode(Integer.parseInt(dto.getSuicidioGrauParentesco())));
//        }
//
//        formSocial.setViolencia(Decisao.getDecisaoCode(Integer.parseInt(dto.getViolencia())));
//        if (Integer.parseInt(dto.getViolencia()) == 1 && dto.getViolenciasCadastradas().isEmpty()) {
//            throw new RegraNegocioException("Campo Violências Cadastradas é obrigatório.");
//        } else if (Integer.parseInt(dto.getViolencia()) == 2 && dto.getViolenciasCadastradas().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Violência deve estar com (Sim == 1) selecionado");
//        } else if (dto.getViolenciasCadastradas().contains("6") && dto.getOutraViolenciaDescricao().isEmpty()) {
//            throw new RegraNegocioException("Campo Outra Violência Descrição deve estar preenchido");
//        } else if (!dto.getViolenciasCadastradas().contains("6") && dto.getOutraViolenciaDescricao().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Violências Cadastradas deve estar preenchido com (Outras == 6)");
//        } else {
//            List<ViolenciasCadastradas> listViolenciasCadastradas = converterCodeViolenciasCadParaList(dto.getViolenciasCadastradas());
//            formSocial.setViolenciasCadastradas(listViolenciasCadastradas);
//            formSocial.setOutraViolenciaDescricao(dto.getOutraViolenciaDescricao());
//        }
//
//        formSocial.setPsicoativos(Decisao.getDecisaoCode(Integer.parseInt(dto.getPsicoativos())));
//        if (Integer.parseInt(dto.getPsicoativos()) == 1 && dto.getPsicoativosCadastrados().isEmpty()) {
//            throw new RegraNegocioException("Campo Psicoativos Cadastradas é obrigatório.");
//        } else if (Integer.parseInt(dto.getPsicoativos()) == 2 && dto.getPsicoativosCadastrados().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Psicoativos deve estar com (Sim == 1) selecionado");
//        } else if (dto.getPsicoativosCadastrados().contains("4") && dto.getOutrosPsicoativosDescricao().isEmpty()) {
//            throw new RegraNegocioException("Campo Outros Psicoativos Descrição deve estar preenchido");
//        } else if (!dto.getPsicoativosCadastrados().contains("4") && dto.getOutrosPsicoativosDescricao().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Psicoativos Cadastradas deve estar preenchido com (Outros == 4)");
//        } else {
//            List<PsicoativosCadastrados> listPsicoativosCadastradas = converterCodePsicoativosCadParaList(dto.getPsicoativosCadastrados());
//            formSocial.setPsicoativosCadastrados(listPsicoativosCadastradas);
//            formSocial.setOutrosPsicoativosDescricao(dto.getOutrosPsicoativosDescricao());
//        }
//
//        formSocial.setConflitoFamiliar(Decisao.getDecisaoCode(Integer.parseInt(dto.getConflitoFamiliar())));
//
//        formSocial.setAtividadesLazer(Decisao.getDecisaoCode(Integer.parseInt(dto.getAtividadesLazer())));
//        if (Integer.parseInt(dto.getAtividadesLazer()) == 1 && dto.getAtividadeLazerCadastradas().isEmpty()) {
//            throw new RegraNegocioException("Campo Atividades Lazer Cadastradas é obrigatório.");
//        } else if (Integer.parseInt(dto.getAtividadesLazer()) == 2 && dto.getAtividadeLazerCadastradas().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Atividades Lazer deve estar com (Sim == 1) selecionado");
//        } else if (dto.getAtividadeLazerCadastradas().contains("5") && dto.getOutrasAtividadeLazerDesc().isEmpty()) {
//            throw new RegraNegocioException("Campo Outras Atividades Lazer Descrição deve estar preenchido");
//        } else if (!dto.getAtividadeLazerCadastradas().contains("5") && dto.getOutrasAtividadeLazerDesc().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Atividades Lazer Cadastradas deve estar preenchido com (Outras == 5)");
//        } else {
//            List<AtividadeLazerCadastradas> listAtivLazeradastradas = converterCodeAtivLazerCadParaList(dto.getAtividadeLazerCadastradas());
//            formSocial.setAtividadeLazerCadastradas(listAtivLazeradastradas);
//            formSocial.setOutrasAtividadeLazerDesc(dto.getOutrasAtividadeLazerDesc());
//        }
//
//        formSocial.setAtividadeFisica(Decisao.getDecisaoCode(Integer.parseInt(dto.getAtividadeFisica())));
//        if (Integer.parseInt(dto.getAtividadeFisica()) == 1 && dto.getAtividadeFisicaDesc().isEmpty()) {
//            throw new RegraNegocioException("Campo Atividade Física Descrição é obrigatório.");
//        } else if (Integer.parseInt(dto.getAtividadeFisica()) == 2 && dto.getAtividadeFisicaDesc().isEmpty() == false) {
//            throw new RegraNegocioException("Campo Atividade Física deve estar com o (Sim == 1) selecionado.");
//        } else {
//            formSocial.setAtividadeFisicaDesc(dto.getAtividadeFisicaDesc());
//        }
//
//
////        formSocial.setQualidadeVida(dto.getQualidadeVida());
//        formSocial.setQualidadeVida(QualidadeVida.getQualidadeVIdaCode(Integer.parseInt(dto.getQualidadeVida())));
//
//        formSocial.setVacinas(Vacinas.getVacinasCode(Integer.parseInt(dto.getVacinas())));

    }


    @Override
    public List<FormSocial> buscarForms() {
//        List<FormSocial> listForms = repository.findAll();
//        return listForms;
        return repository.findAll();
    }

    public EstadoCivil persistEstadoCivil(FormSocialDTO dto) {
        EstadoCivil estadoCivil = new EstadoCivil();

        if (dto.getEstadoCivil() < 1 || dto.getEstadoCivil() > 6) {
            throw new EstadoCivilException("ID do Estado Civil é inválido");
        }

        Optional<EstadoCivil> estadoBD = estadoCivilRepository.findById(dto.getEstadoCivil());

        if (estadoBD.isPresent()) {
            estadoCivil.setId(estadoBD.get().getId());
            estadoCivil.setEstadoCivil(estadoBD.get().getEstadoCivil());
        }

        return estadoCivil;
    }

    public Escolaridade persistEscolaridade(FormSocialDTO dto) {
        Escolaridade escolaridade = new Escolaridade();

        if (dto.getEscolaridade() < 1 || dto.getEscolaridade() > 7) {
            throw new EscolaridadeException("ID da Escolaridade é inválido");
        }

        Optional<Escolaridade> escolaridadeBD = escolaridadeRepository.findById(dto.getEscolaridade());

        if (escolaridadeBD.isPresent()) {
            escolaridade.setId(escolaridadeBD.get().getId());
            escolaridade.setEscolaridade(escolaridadeBD.get().getEscolaridade());
        }

        return escolaridade;
    }

    public Residencia persistResidencia(FormSocialDTO dto) {
        Residencia residencia = new Residencia();

        if (dto.getResidencia() < 1 || dto.getResidencia() > 4) {
            throw new ResidenciaException("ID da Residência é inválido");
        }

        Optional<Residencia> residenciaBD = residenciaRepository.findById(dto.getResidencia());

        if (residenciaBD.isPresent()) {
            residencia.setId(residenciaBD.get().getId());
            residencia.setResidencia(residenciaBD.get().getResidencia());
        }

        return residencia;
    }

    public List<GrauParentesco> convertIndexGrauParentesco(FormSocialDTO dto) {
        List<GrauParentesco> listGrauParentesco = new ArrayList<>();

        for (Integer index : dto.getGrauParentesco()) {
            if (index < 1 || index > 12) {
                throw new GrauParentescoException("Algum dos ID's de Grau Parentesco é inválido. (1-12)");
            }

            GrauParentesco grauParentesco = new GrauParentesco();

            Optional<GrauParentesco> grauParentescoBD = grauParentescoRepository.findById(index);

            if (grauParentescoBD.isPresent()) {
                grauParentesco.setId(grauParentescoBD.get().getId());
                grauParentesco.setGrauParentesco(grauParentescoBD.get().getGrauParentesco());
                listGrauParentesco.add(grauParentesco);
            }
        }

        return listGrauParentesco;
    }

    public List<Beneficio> convertIndexBeneficio(FormSocialDTO dto) {
        List<Beneficio> listBeneficios = new ArrayList<>();

        for (Integer index : dto.getBeneficio()) {
            Beneficio beneficio = new Beneficio();

            if (index < 1 || index > 5) {
                throw new BeneficioException("Algum dos ID's de Beneficio é inválido. (1-5)");
            } else if (index == 1 && dto.getBeneficio().size() > 1) {
                throw new BeneficioException("O campo ID 1 (Nenhum) está selecionado, por isso não é possível adicionar os outros benefícios disponíveis (1-5)");
            } else if (index == 5 && dto.getOutroBeneficio().isEmpty()) {
                throw new BeneficioException("O ID 5 (Outros) está selecionado e o campo Outro Benefício está vazio, preencha-o");
            } else if (!dto.getBeneficio().contains(5) && !dto.getOutroBeneficio().isEmpty()) {
                throw new BeneficioException("O ID 5 (Outros) não está selecionado, selecione-o para poder preencher o campo Outro Benefício");
            }

            Optional<Beneficio> beneficioBD = beneficioRepository.findById(index);

            if (beneficioBD.isPresent() && beneficioBD.get().getId() != 5) {
                beneficio.setId(beneficioBD.get().getId());
                beneficio.setBeneficio(beneficioBD.get().getBeneficio());

                listBeneficios.add(beneficio);
            }

            if (beneficioBD.get().getId() == 5) {
                beneficio.setBeneficio(dto.getOutroBeneficio());
                beneficioRepository.save(beneficio);
                listBeneficios.add(beneficio);
            }
        }
        return listBeneficios;
    }

    public List<ProgramaSocial> convertIndexProgramaSocial(FormSocialDTO dto) {
        List<ProgramaSocial> listProgramas = new ArrayList<>();

        for (Integer index : dto.getProgramaSocial()) {
            ProgramaSocial programaSocial = new ProgramaSocial();

            if (index < 1 || index > 5) {
                throw new ProgramaSocialException("Algum dos ID's dos Programas Sociais é inválido. (1-5)");
            } else if (index == 1 && dto.getProgramaSocial().size() > 1) {
                throw new ProgramaSocialException("O campo ID 1 (Não) está selecionado, por isso não é possível adicionar os outros Programas Sociais disponíveis (1-5)");
            } else if (index == 5 && dto.getOutroProgramaSocial().isEmpty()) {
                throw new ProgramaSocialException("O ID 5 (Outros) está selecionado e o campo Outro Programa Social está vazio, preencha-o ");
            } else if (!dto.getProgramaSocial().contains(5) && !dto.getOutroProgramaSocial().isEmpty()) {
                throw new ProgramaSocialException("O ID 5 (Outros) não está selecionado, selecione-o para poder preencher o campo Outro Programa Social");
            }

            Optional<ProgramaSocial> programaSocialBD = programaSocialRepository.findById(index);

            if (programaSocialBD.isPresent() && programaSocialBD.get().getId() != 5) {
                programaSocial.setId(programaSocialBD.get().getId());
                programaSocial.setProgramaSocial(programaSocialBD.get().getProgramaSocial());

                listProgramas.add(programaSocial);
            }

            if (programaSocialBD.get().getId() == 5) {
                programaSocial.setProgramaSocial(dto.getOutroProgramaSocial());
                programaSocialRepository.save(programaSocial);
                listProgramas.add(programaSocial);
            }
        }
        return listProgramas;
    }

    public List<DoencaCronica> convertIndexDoencaCronica(FormSocialDTO dto) {
        List<DoencaCronica> listDoenca = new ArrayList<>();

        for (Integer index : dto.getDoencaCronica()) {
            DoencaCronica doencaCronica = new DoencaCronica();

            if (index < 1 || index > 6) {
                throw new DoencaCronicaException("Algum dos ID's das Doenças Crônicas é inválido. (1-6)");
            } else if (index == 1 && dto.getDoencaCronica().size() > 1) {
                throw new DoencaCronicaException("O campo ID 1 (Nenhuma) está selecionado, por isso não é possível adicionar as outras Doenças Crônicas disponíveis (1-6)");
            } else if (index == 6 && dto.getOutraDoencaCronica().isEmpty()) {
                throw new DoencaCronicaException("O ID 6 (Outras) está selecionado e o campo Outra Doença Crônica está vazio, preencha-o ");
            } else if (!dto.getDoencaCronica().contains(6) && !dto.getOutraDoencaCronica().isEmpty()) {
                throw new DoencaCronicaException("O ID 6 (Outras) não está selecionado, selecione-o para poder preencher o campo Outra Doença Crônica");
            }

            Optional<DoencaCronica> doencaCronicaBD = doencaCronicaRepository.findById(index);

            if (doencaCronicaBD.isPresent() && doencaCronicaBD.get().getId() != 6) {
                doencaCronica.setId(doencaCronicaBD.get().getId());
                doencaCronica.setDoencaCronica(doencaCronicaBD.get().getDoencaCronica());

                listDoenca.add(doencaCronica);
            }

            if (doencaCronicaBD.get().getId() == 6) {
                doencaCronica.setDoencaCronica(dto.getOutraDoencaCronica());
                doencaCronicaRepository.save(doencaCronica);
                listDoenca.add(doencaCronica);
            }
        }
        return listDoenca;
    }

    public DeficienteFamilia persistDeficienteFamilia(FormSocialDTO dto) {
        DeficienteFamilia deficienteFamilia = new DeficienteFamilia();

        if (dto.getDeficienteFamilia() < 1 || dto.getDeficienteFamilia() > 2) {
            throw new DeficienteFamiliaException("ID do Deficiente da Familia é inválido");
        } else if (dto.getDeficienteFamilia() == 2 && dto.getPessoaDeficiente().isEmpty() && dto.getDeficiencia().isEmpty()) {
            throw new DeficienteFamiliaException("O ID 2 (Sim) está selecionado, mas os campos Pessoa Deficiente e Deficiência estão vazios, preencha-os");
        } else if (dto.getDeficienteFamilia() != 2 && !dto.getPessoaDeficiente().isEmpty() && !dto.getDeficiencia().isEmpty()) {
            throw new DeficienteFamiliaException("O ID 2 (Sim) não está selecionado, selecione-o para poder preencher os campos Pessoa Deficiente e Deficiência");
        }

        Optional<DeficienteFamilia> deficienteBD = deficienteFamiliaRepository.findById(dto.getDeficienteFamilia());

        if (deficienteBD.get().getId() == 1) {
            deficienteFamilia.setId(deficienteBD.get().getId());
            deficienteFamilia.setPessoa(deficienteBD.get().getPessoa());
            deficienteFamilia.setDeficiencia(deficienteBD.get().getDeficiencia());
        } else if (deficienteBD.get().getId() == 2) {
            deficienteFamilia.setPessoa(dto.getPessoaDeficiente());
            deficienteFamilia.setDeficiencia(dto.getDeficiencia());
        }

        deficienteFamiliaRepository.save(deficienteFamilia);
        return deficienteFamilia;
    }

    public AcompMedico persistAcompMedico(FormSocialDTO dto) {
        AcompMedico acompMedico = new AcompMedico();

        if (dto.getAcompMedico() < 1 || dto.getAcompMedico() > 2) {
            throw new AcompMedicoException("ID do Acompanhamento Médico é inválido");
        } else if (dto.getAcompMedico() == 2 && dto.getEspecialidadeAcompMedico().isEmpty()) {
            throw new AcompMedicoException("O ID 2 (Sim) está selecionado, mas o campo Especialidade Acompanhamento Médico esta vazio, preencha-o");
        } else if (dto.getAcompMedico() != 2 && !dto.getEspecialidadeAcompMedico().isEmpty()) {
            throw new AcompMedicoException("O ID 2 (Sim) não está selecionado, selecione-o para poder preencher o campo Especialidade Acompanhamento Médico");
        }

        Optional<AcompMedico> acompanhamentoBD = acompMedicoRepository.findById(dto.getAcompMedico());

        if (acompanhamentoBD.get().getId() == 1) {
            acompMedico.setId(acompanhamentoBD.get().getId());
            acompMedico.setEspecialidadeMedica(acompanhamentoBD.get().getEspecialidadeMedica());
        } else if (acompanhamentoBD.get().getId() == 2) {
            acompMedico.setEspecialidadeMedica(dto.getEspecialidadeAcompMedico());
        }
        acompMedicoRepository.save(acompMedico);
        return acompMedico;

    }

    public SuicidioFamilia persistSuicidioFam(FormSocialDTO dto) {
        SuicidioFamilia suicidioFamilia = new SuicidioFamilia();
        GrauParentesco grauParentesco = new GrauParentesco();

        if (dto.getSuicidioFamilia() < 1 || dto.getSuicidioFamilia() > 2) {
            throw new SuicidioFamiliaException("ID do Suicidio Familia é inválido");
        }

        if (dto.getSuicidioFamilia() == 2 && !dto.getGrauParentescoSuicidio().isPresent()) {
            throw new SuicidioFamiliaException("O ID 2 (Sim) está selecionado, mas o campo Grau Parentesco Suicidio está vazio, preencha-o");
        } else if (dto.getSuicidioFamilia() != 2 && dto.getGrauParentescoSuicidio().isPresent()) {
            throw new SuicidioFamiliaException("O ID 2 (Sim) não está selecionado, selecione-o para poder preencher o campo Grau Parentesco Suicídio");
        }
        // verifica se o grau parentesco suicidio não é vazio e se é válido
        if (dto.getGrauParentescoSuicidio().isPresent()) {
            if (dto.getGrauParentescoSuicidio().get() < 1 || dto.getGrauParentescoSuicidio().get() > 12) {
                throw new GrauParentescoException("ID do Grau Parentesco Suicídio é inválido. (1-12)");
            }
        }

        Optional<SuicidioFamilia> suicidioBD = suicidioFamiliaRepository.findById(dto.getSuicidioFamilia());

        if (dto.getGrauParentescoSuicidio().isPresent()) {
            Optional<GrauParentesco> grauParentescoBD = grauParentescoRepository.findById(dto.getGrauParentescoSuicidio().get());

            grauParentesco.setId(grauParentescoBD.get().getId());
            grauParentesco.setGrauParentesco(grauParentescoBD.get().getGrauParentesco());
        }

        if (suicidioBD.get().getId() == 1) {
            suicidioFamilia.setId(suicidioBD.get().getId());
            suicidioFamilia.setSuicidio(suicidioBD.get().getSuicidio());
        } else if (suicidioBD.get().getId() == 2) {
            suicidioFamilia.setSuicidio(suicidioBD.get().getSuicidio());
            suicidioFamilia.setGrauParentescoSuicidio(grauParentesco);
        }

        suicidioFamiliaRepository.save(suicidioFamilia);


        return suicidioFamilia;

    }

//
//    public InfoFormSocialDTO converterFormInfo(FormSocial form){
//
//        return InfoFormSocialDTO.builder()
//                .id(form.getId())
//                .nome(form.getNome())
//                .idade(DateUtil.calculaIdade(form.getDataNascimento()))
//                .dataNascismento(DateUtil.formatter.format(form.getDataNascimento()))
//                .dataEntrevista(DateUtil.formatter.format(form.getDataEntrevista()))
//                .telefone(form.getTelefone())
//                .email(form.getEmail())
//                .funcaoExerc(form.getFuncaoExerc())
//                .tempoFuncaoExerc(form.getTempoFuncaoExerc())
//                .estadoCivil(form.getEstadoCivil().toString())
//                .escolaridade(form.getEscolaridade().toString())
//                .numeroPessoasFam(form.getNumeroPessoasFam())
//                .grauParentesco(form.getGrauParentesco().toString())
//                .residencia(form.getResidencia().toString())
//                .beneficio(form.getBeneficio().toString())
//                .beneficiosCadastrados(form.getBeneficiosCadastrados().toString())
//                .outroBeneficioDesc(form.getOutroBeneficioDesc())
//                .programaSocial(form.getProgramaSocial().toString())
//                .programasSociaisCadastrados(form.getProgramasSociaisCadastrados().toString())
//                .outroProgramaSocialDesc(form.getOutroProgramaSocialDesc())
//                .doencaCronica(form.getDoencaCronica().toString())
//                .doencaCronicaCadastradas(form.getDoencasCronicasCadastradas().toString())
//                .outraDoencaCronicaDesc(form.getOutraDoencaCronicasDesc())
//                .deficienteFamilia(form.getDeficienteFamilia().toString())
//                .deficienteFamiliaDescricao(form.getDeficienteFamiliaDescricao())
//                .acompanhamentoMedico(form.getAcompMedico().toString())
//                .acompanhamentoMedicoDesc(form.getAcompMedicoDescricao())
//                .suicidioFamilia(form.getSuicidioFamilia().toString())
//                .suicidioGrauParentesco(form.getSuicidioGrauParentesco().toString())
//                .violencia(form.getViolencia().toString())
//                .violenciasCadastradas(form.getViolenciasCadastradas().toString())
//                .outraViolenciaDescricao(form.getOutraViolenciaDescricao())
//
//                .psicoativos(form.getPsicoativos().toString())
//                .psicoativosCadastrados(form.getPsicoativosCadastrados().toString())
//                .outrosPsicoativosDescricao(form.getOutrosPsicoativosDescricao())
//
//                .conflitoFamiliar(form.getConflitoFamiliar().toString())
//
//                .atividadesLazer(form.getAtividadesLazer().toString())
//                .atividadesLazerCadastradas(form.getAtividadeLazerCadastradas().toString())
//                .outrasAtividadesLazerDesc(form.getOutrasAtividadeLazerDesc())
//
//                .atividadeFisica(form.getAtividadeFisica().toString())
//                .atividadeFisicaDesc(form.getAtividadeFisicaDesc())
//                .qualidadeVida(form.getQualidadeVida().toString())
//                .vacinas(form.getVacinas().toString())
//                .build();
//    }
//
//    public List<GrauParentesco> converterCodeGrauParList(String codes) {
//        String[] indices = codes.split("-");
//        List<GrauParentesco> listaGrauPar = new ArrayList<>();
//        for (String code : indices) {
//            int num = Integer.parseInt(code);
//            listaGrauPar.add(GrauParentesco.getGrauParentescoCode(num));
//        }
//        return listaGrauPar;
//    }
//
//    public List<BeneficiosCadastrados> converterCodeBenefParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<BeneficiosCadastrados> listaBeneficiosCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaBeneficiosCad.add(BeneficiosCadastrados.getBeneficiosCadastradosCode(num));
//            }
//        }
//
//        return listaBeneficiosCad;
//    }
//
//    public List<ProgramasSociaisCadastrados> converterCodeProgSocialParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<ProgramasSociaisCadastrados> listaProgramasSociaisCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaProgramasSociaisCad.add(ProgramasSociaisCadastrados.getProgramasSociaisCadCode(num));
//            }
//        }
//
//        return listaProgramasSociaisCad;
//    }
//
//    public List<DoencasCronicasCadastradas> converterCodeDoencaCronicaParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<DoencasCronicasCadastradas> listaDoencaCronicasCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaDoencaCronicasCad.add(DoencasCronicasCadastradas.getDoencaCronicaCode(num));
//            }
//        }
//
//        return listaDoencaCronicasCad;
//    }
//
//    public List<ViolenciasCadastradas> converterCodeViolenciasCadParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<ViolenciasCadastradas> listaViolenciasCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaViolenciasCad.add(ViolenciasCadastradas.getViolenciaCode(num));
//            }
//        }
//
//        return listaViolenciasCad;
//    }
//
//    public List<PsicoativosCadastrados> converterCodePsicoativosCadParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<PsicoativosCadastrados> listaPsicoativosCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaPsicoativosCad.add(PsicoativosCadastrados.getPsicoativosCode(num));
//            }
//        }
//
//        return listaPsicoativosCad;
//    }
//
//    public List<AtividadeLazerCadastradas> converterCodeAtivLazerCadParaList(String codes) {
//        String[] indices = codes.split("-");
//        List<AtividadeLazerCadastradas> listaAtivLazerCad = new ArrayList<>();
//        if (!codes.isEmpty()) {
//            for (String code : indices) {
//                int num = Integer.parseInt(code);
//                listaAtivLazerCad.add(AtividadeLazerCadastradas.getAtividadeLazerCadastradasCode(num));
//            }
//        }
//
//        return listaAtivLazerCad;
//    }

}