/**
${pojo.getClassJavaDoc(pojo.getDeclarationName() + " generated by hbm2java", 0)} 
 */
${pojo.getClassModifiers()}<#if pojo.isAbstract()> abstract</#if> ${pojo.getDeclarationType()} ${pojo.getDeclarationName()}VO ${pojo.getExtendsDeclaration()?replace("Impl", "VO")} implements ${pojo.getDeclarationName()}I<#if !pojo.isSubclass()>, ch.algotrader.entity.BaseValueObjectI</#if> {

    private static final long serialVersionUID = 1L;
