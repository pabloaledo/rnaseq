process UCSC_BEDGRAPHTOBIGWIG {
    tag "$meta.id"
    label 'process_single'

    // WARN: Version information not provided by tool on CLI. Please update version string below when bumping container versions.
    conda "ucsc-bedgraphtobigwig=377"

    input:
    tuple val(meta), path(bedgraph)
    path  sizes

    output:
    tuple val(meta), path("*.bigWig"), emit: bigwig
    path "versions.yml"              , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    def args = task.ext.args ?: ''
    def prefix = task.ext.prefix ?: "${meta.id}"
    def VERSION = '377' // WARN: Version information not provided by tool on CLI. Please update this string when bumping container versions.
    """
    bedGraphToBigWig \\
        $bedgraph \\
        $sizes \\
        ${prefix}.bigWig

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        ucsc: $VERSION
    END_VERSIONS
    """
}
