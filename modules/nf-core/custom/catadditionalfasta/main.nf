process CUSTOM_CATADDITIONALFASTA {
    tag "$meta.id"

    conda "conda-forge::python=3.9.5"

    input:
    tuple val(meta), path(fasta), path(gtf)
    tuple val(meta2), path(add_fasta)
    val  biotype

    output:
    tuple val(meta), path("*/*.fasta") , emit: fasta
    tuple val(meta), path("*/*.gtf")   , emit: gtf
    path "versions.yml"                , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    template 'fasta2gtf.py'
}
