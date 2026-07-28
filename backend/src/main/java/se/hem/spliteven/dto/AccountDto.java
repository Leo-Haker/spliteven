package se.hem.spliteven.dto;

import java.util.List;

public record AccountDto(Long id, String name, List<PersonDto> members) {
}