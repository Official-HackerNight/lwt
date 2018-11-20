package com.hackernight.lwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Episode entity.
 * @author Steven Kelsey.
 * @updated November 20th, 2018
 */
@ApiModel(description = "Episode entity. @author Steven Kelsey. @updated November 20th, 2018")
@Entity
@Table(name = "episode")
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "air_date", nullable = false)
    private LocalDate airDate;

    @NotNull
    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;

    @NotNull
    @Column(name = "episode_season", nullable = false)
    private Integer episodeSeason;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "call_to_action")
    private String callToAction;

    @Column(name = "call_to_action_due_date")
    private LocalDate callToActionDueDate;

    @ManyToMany
    @JoinTable(name = "episode_tag",
               joinColumns = @JoinColumn(name = "episodes_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "episode")
    private Set<Resource> resources = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Episode title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Episode description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAirDate() {
        return airDate;
    }

    public Episode airDate(LocalDate airDate) {
        this.airDate = airDate;
        return this;
    }

    public void setAirDate(LocalDate airDate) {
        this.airDate = airDate;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public Episode episodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
        return this;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getEpisodeSeason() {
        return episodeSeason;
    }

    public Episode episodeSeason(Integer episodeSeason) {
        this.episodeSeason = episodeSeason;
        return this;
    }

    public void setEpisodeSeason(Integer episodeSeason) {
        this.episodeSeason = episodeSeason;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Episode thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public Episode youtubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
        return this;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getCallToAction() {
        return callToAction;
    }

    public Episode callToAction(String callToAction) {
        this.callToAction = callToAction;
        return this;
    }

    public void setCallToAction(String callToAction) {
        this.callToAction = callToAction;
    }

    public LocalDate getCallToActionDueDate() {
        return callToActionDueDate;
    }

    public Episode callToActionDueDate(LocalDate callToActionDueDate) {
        this.callToActionDueDate = callToActionDueDate;
        return this;
    }

    public void setCallToActionDueDate(LocalDate callToActionDueDate) {
        this.callToActionDueDate = callToActionDueDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Episode tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Episode addTag(Tag tag) {
        this.tags.add(tag);
        tag.getEpisodes().add(this);
        return this;
    }

    public Episode removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getEpisodes().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Episode resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Episode addResource(Resource resource) {
        this.resources.add(resource);
        resource.setEpisode(this);
        return this;
    }

    public Episode removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.setEpisode(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Episode episode = (Episode) o;
        if (episode.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), episode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Episode{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", airDate='" + getAirDate() + "'" +
            ", episodeNumber=" + getEpisodeNumber() +
            ", episodeSeason=" + getEpisodeSeason() +
            ", thumbnailUrl='" + getThumbnailUrl() + "'" +
            ", youtubeUrl='" + getYoutubeUrl() + "'" +
            ", callToAction='" + getCallToAction() + "'" +
            ", callToActionDueDate='" + getCallToActionDueDate() + "'" +
            "}";
    }
}
